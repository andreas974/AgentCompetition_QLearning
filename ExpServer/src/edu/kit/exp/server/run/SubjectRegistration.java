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

/**
 * This class matches clients to subjects.
 *
 */
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

		numberOfSubjects = subjectList.size();

		if (runStateLogger.getContinueSessionFlag() == false) {

			randomSubjectNumbers = randomNumberGenerator.generateNonRepeatingIntegers(0, numberOfSubjects - 1);

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
