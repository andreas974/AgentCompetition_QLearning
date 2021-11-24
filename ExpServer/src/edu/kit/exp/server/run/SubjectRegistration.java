package edu.kit.exp.server.run;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import javax.naming.CommunicationException;

import edu.kit.exp.impl.continuousCompetition.server.ContinuousCompetitionRoleMatcher;
import edu.kit.exp.server.communication.ClientRegistrationMessage;
import edu.kit.exp.server.communication.ServerCommunicationManager;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.SubjectManagement;

/**
 * This class matches clients to subjects.
 *
 */

// !!! This class has been modified to allow for human/agent matching in UpstreamCompetition experiment !!!
public class SubjectRegistration {

	private RandomNumberGenerator randomNumberGenerator;
	private RunStateLogger runStateLogger;
	private SubjectManagement subjectManagement;
	private static SubjectRegistration instance;
	private ArrayList<Subject> subjectList;
	// UC modification add: start
	private ArrayList<Subject> humanSubjectList;
	private ArrayList<Subject> agentSubjectList;
	private ArrayList<Integer> randomAgentSubjectNumbers;
	private int numberOfRegisteredAgents;
	private ArrayList<Integer> randomHumanSubjectNumbers;
	private int numberOfRegisteredHumans;
	// UC modification add: end
	private int numberOfSubjects;
	private int numberOfRegisteredSubjects;
	// UC modification delete: start
	// private ArrayList<Integer> randomSubjectNumbers;
	// UC modification delete: end
	private ArrayList<String> listOfConnectedClients;

	private SubjectRegistration() {
		randomNumberGenerator = RandomNumberGenerator.getInstance();
		runStateLogger = RunStateLogger.getInstance();
		subjectManagement = SubjectManagement.getInstance();
		listOfConnectedClients = new ArrayList<String>();
	}

	public synchronized static SubjectRegistration getInstance() {
		if (instance == null){
			instance = new SubjectRegistration();
		}
		return instance;
	}


	/**
	 * Prepares the matching.
	 * @param session
	 * @throws RandomGeneratorException
	 */
	void prepareClientToSubjectMatching(Session session) throws RandomGeneratorException {

		RunStateLogger.getInstance().setAllSubjectsConnected(false);
		subjectList = new ArrayList<Subject>();

		for (Cohort cohort : session.getCohorts()) {
			subjectList.addAll(cohort.getSubjects());
		}

		//Anzahl der Rollen abfragen
		int count = 0;
		ArrayList<String> AllRoles = new ArrayList<>();
		for (Subject s: subjectList){
			AllRoles.add(s.getRole());
		}

		int res = 1;
		// Pick all elements one by one
		for (int i = 1; i < AllRoles.size(); i++)
		{
			int j = 0;
			for (j = 0; j < i; j++)
				if (AllRoles.get(i) == AllRoles.get(j))
					break;

			// If not printed earlier,
			// then print it
			if (i == j)
				res++;
		}

		System.out.println("Anzahl Firmen" + res);

		//Select Random Firm
		String[] firmarray = new String[res];
		if (res==3){
			firmarray[0]="Firma A";
			firmarray[1]="Firma B";
			firmarray[2]="Firma C";
		}
		else {
			firmarray[0] = "Firma A";
			firmarray[1] = "Firma B";
		}

		int rnd = new Random().nextInt(firmarray.length);
		String RandomFirm = firmarray[rnd];


		// UC modification add: start
		// Divide subject list according to role:
		// humanSubjectList is designed to contain subjects that have been assigned with roles of firm A and firm B
		// agentSubjectList is designed to contain subjects that have been assigned with role of firm E
		humanSubjectList = new ArrayList<Subject>();
		agentSubjectList = new ArrayList<Subject>();

		for (Subject s: subjectList) {
			if (s.getRole().equals(RandomFirm)) {
				agentSubjectList.add(s);
			} else {
				humanSubjectList.add(s);
			}
		}
		// UC modification add: end

		numberOfSubjects = subjectList.size();

		if (runStateLogger.getContinueSessionFlag() == false) {

			// UC modification replace: start
			//randomSubjectNumbers = randomNumberGenerator.generateNonRepeatingIntegers(0, numberOfSubjects - 1);
			randomAgentSubjectNumbers = randomNumberGenerator.generateNonRepeatingIntegers(0, agentSubjectList.size() - 1);
			randomHumanSubjectNumbers = randomNumberGenerator.generateNonRepeatingIntegers(0, humanSubjectList.size() - 1);
			// UC modification replace: end
		}

	}

	/**
	 * This method matches connecting clients subject.
	 * If the session is (re)started all subjects are matched randomly.
	 * If a session should be continued, all clients are matched to the same subject again.
	 * @param clientRegistrationMessage
	 * @throws CommunicationException
	 */
	public void registerSubject(ClientRegistrationMessage clientRegistrationMessage) throws CommunicationException {

		String clientId = clientRegistrationMessage.getClientId();
		ServerMessageSender messageSender = ServerCommunicationManager.getInstance().getMessageSender();

		if (runStateLogger.getContinueSessionFlag() == false && runStateLogger.getAllSubjectsConnected() == false) {

			messageSender.registerClient(clientRegistrationMessage);

			// UC modification replace: start
			// Added custom matching for UpstreamCompetition experiment
			// if client id identifies client as agent assign role of firm E, otherwise assign firm A or firm B

			// Deleted:
			// int randomNumber = randomSubjectNumbers.get(numberOfRegisteredSubjects);
			// Subject subject = subjectList.get(randomNumber);
			Subject subject;
			int randomNumber;
			if (clientId.startsWith("agent")) {
				randomNumber = randomAgentSubjectNumbers.get(numberOfRegisteredAgents);
				subject = agentSubjectList.get(randomNumber);
				numberOfRegisteredAgents++;
				System.out.println("Registered agent: clientId = "+clientId+", assigned role = "+subject.getRole()+" in cohort "+subject.getCohort().getIdCohort());

			} else {
				System.out.println("Register human");
				randomNumber = randomHumanSubjectNumbers.get(numberOfRegisteredHumans);
				subject = humanSubjectList.get(randomNumber);
				numberOfRegisteredHumans++;
				System.out.println("Registered human: clientId = "+clientId+", assigned role = "+subject.getRole()+" in cohort "+subject.getCohort().getIdCohort());
			}
			// UC modification replace: end

			subject.setIdClient(clientId);
			Subject updatedSubject;
			try {
				updatedSubject = subjectManagement.updateSubject(subject);
			} catch (StructureManagementException e) {
				throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
			}

			numberOfRegisteredSubjects++;
			RunStateLogger.getInstance().createOutputMessage("Client " + numberOfRegisteredSubjects + "/" + numberOfSubjects + " connected! (" + clientId + ")");

			if (numberOfRegisteredSubjects == numberOfSubjects) {
				RunStateLogger.getInstance().setAllSubjectsConnected(true);
			}

			try {
				messageSender.sendWelcomeMessage(updatedSubject);
			} catch (RemoteException e) {
				throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
			}

			listOfConnectedClients.add(clientId);

		} else {

			// Reconnect OR CONTINUE
			boolean knownClient = false;
			Subject knownSubject = null;

			for (Subject subject : subjectList) {
				if (subject.getIdClient().equals(clientId)) {
					knownSubject = subject;
					knownClient = true;
					break;
				}
			}

			if (!knownClient) {
				throw new CommunicationException("Unknown client id!");
			} else {

				// Known Subject

				messageSender.registerClient(clientRegistrationMessage);

				if (listOfConnectedClients.contains(clientId)) {
					RunStateLogger.getInstance().createOutputMessage("Reconnect! (" + clientId + ")");
				} else {
					listOfConnectedClients.add(clientId);
					numberOfRegisteredSubjects++;
					RunStateLogger.getInstance().createOutputMessage("Client " + numberOfRegisteredSubjects + "/" + numberOfSubjects + " connected! (" + clientId + ")");
				}

				if (numberOfRegisteredSubjects == numberOfSubjects) {
					RunStateLogger.getInstance().setAllSubjectsConnected(true);
				}

				try {
					messageSender.sendWelcomeMessage(knownSubject);
				} catch (RemoteException e) {
					throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
				}

			}

		}

	}
}


//Old version below:
/*
package edu.kit.exp.server.run;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.naming.CommunicationException;

import edu.kit.exp.server.communication.ClientRegistrationMessage;
import edu.kit.exp.server.communication.ServerCommunicationManager;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.SubjectManagement;

*/
/**
 * This class matches clients to subjects.
 *
 *//*

public class SubjectRegistration {

	private RandomNumberGenerator randomNumberGenerator;
	private RunStateLogger runStateLogger;
	private SubjectManagement subjectManagement;
	private static SubjectRegistration instance;
	private ArrayList<Subject> subjectList;
	private int numberOfSubjects;
	private int numberOfRegisteredSubjects;
	private ArrayList<Integer> randomSubjectNumbers;
	private ArrayList<String> listOfConnectedClients;

	private SubjectRegistration() {
		randomNumberGenerator = RandomNumberGenerator.getInstance();
		runStateLogger = RunStateLogger.getInstance();
		subjectManagement = SubjectManagement.getInstance();
		listOfConnectedClients = new ArrayList<String>();
	}

	public synchronized static SubjectRegistration getInstance() {
		if (instance == null){
			instance = new SubjectRegistration();
		}
		return instance;
	}

	*/
/**
	 * Prepares the matching.
	 * @param session
	 * @throws RandomGeneratorException
	 *//*

	void prepareClientToSubjectMatching(Session session) throws RandomGeneratorException {

		RunStateLogger.getInstance().setAllSubjectsConnected(false);
		subjectList = new ArrayList<Subject>();

		for (Cohort cohort : session.getCohorts()) {
			subjectList.addAll(cohort.getSubjects());
		}

		numberOfSubjects = subjectList.size();

		if (runStateLogger.getContinueSessionFlag() == false) {

			randomSubjectNumbers = randomNumberGenerator.generateNonRepeatingIntegers(0, numberOfSubjects - 1);

		}

	}

	*/
/**
	 * This method matches connecting clients subject.
	 * If the session is (re)started all subjects are matched randomly.
	 * If a session should be continued, all clients are matched to the same subject again.
	 * @param clientRegistrationMessage
	 * @throws CommunicationException
	 *//*

	public void registerSubject(ClientRegistrationMessage clientRegistrationMessage) throws CommunicationException {

		String clientId = clientRegistrationMessage.getClientId();
		ServerMessageSender messageSender = ServerCommunicationManager.getInstance().getMessageSender();
		
		if (runStateLogger.getContinueSessionFlag() == false && runStateLogger.getAllSubjectsConnected() == false) {

			messageSender.registerClient(clientRegistrationMessage);

			int randomNumber = randomSubjectNumbers.get(numberOfRegisteredSubjects);
			Subject subject = subjectList.get(randomNumber);
			subject.setIdClient(clientId);
			Subject updatedSubject;

			try {
				updatedSubject = subjectManagement.updateSubject(subject);
			} catch (StructureManagementException e) {
				throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
			}

			numberOfRegisteredSubjects++;
			RunStateLogger.getInstance().createOutputMessage("Client " + numberOfRegisteredSubjects + "/" + numberOfSubjects + " connected! (" + clientId + ")");

			if (numberOfRegisteredSubjects == numberOfSubjects) {
				RunStateLogger.getInstance().setAllSubjectsConnected(true);
			}

			try {
				messageSender.sendWelcomeMessage(updatedSubject);
			} catch (RemoteException e) {
				throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
			}

			listOfConnectedClients.add(clientId);

		} else {

			// Reconnect OR CONTINUE
			boolean knownClient = false;
			Subject knownSubject = null;

			for (Subject subject : subjectList) {
				if (subject.getIdClient().equals(clientId)) {
					knownSubject = subject;
					knownClient = true;
					break;
				}
			}

			if (!knownClient) {
				throw new CommunicationException("Unknown client id!");
			} else {

				// Known Subject

				messageSender.registerClient(clientRegistrationMessage);

				if (listOfConnectedClients.contains(clientId)) {
					RunStateLogger.getInstance().createOutputMessage("Reconnect! (" + clientId + ")");
				} else {
					listOfConnectedClients.add(clientId);
					numberOfRegisteredSubjects++;
					RunStateLogger.getInstance().createOutputMessage("Client " + numberOfRegisteredSubjects + "/" + numberOfSubjects + " connected! (" + clientId + ")");
				}

				if (numberOfRegisteredSubjects == numberOfSubjects) {
					RunStateLogger.getInstance().setAllSubjectsConnected(true);
				}

				try {
					messageSender.sendWelcomeMessage(knownSubject);
				} catch (RemoteException e) {
					throw new CommunicationException("Client registration failed! Cause:" + e.getMessage());
				}

			}

		}

	}
}
*/
