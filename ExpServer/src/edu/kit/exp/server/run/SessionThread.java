package edu.kit.exp.server.run;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.kit.exp.client.gui.screens.DefaultInfoScreen;
import edu.kit.exp.server.communication.ClientMessage;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ClientTrialLogMessage;
import edu.kit.exp.server.communication.QuizProtocolMessage;
import edu.kit.exp.server.communication.ServerCommunicationManager;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Pause;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Protocol;
import edu.kit.exp.server.jpa.entity.Quiz;
import edu.kit.exp.server.jpa.entity.SequenceElement;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;
import edu.kit.exp.server.jpa.entity.Trial;
import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.EnvironmentFactory;
import edu.kit.exp.server.microeconomicsystem.Institution;
import edu.kit.exp.server.microeconomicsystem.InstitutionFactory;
import edu.kit.exp.server.structure.PeriodManagement;
import edu.kit.exp.server.structure.ProtocolManagement;
import edu.kit.exp.server.structure.SequenceElementManagement;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.SubjectGroupManagement;
import edu.kit.exp.server.structure.TrialManagement;

/**
 * This class runs sequentially through the elements of a session. 
 *
 */
public class SessionThread extends Thread {

	private SessionQueue periodQueue;
	private HashMap<String, Institution<? extends Environment>> gameMap = new HashMap<String, Institution<? extends Environment>>();
	private SubjectGroupManagement subjectGroupManagement = SubjectGroupManagement.getInstance();
	private ServerMessageSender messageSender = ServerCommunicationManager.getInstance().getMessageSender();
	private TrialManagement trialManagement = TrialManagement.getInstance();
	private PeriodManagement periodManagement = PeriodManagement.getInstance();
	private RunStateLogger runStateLogger = RunStateLogger.getInstance();
	private SequenceElementManagement sequenceElementManagement = SequenceElementManagement.getInstance();
	private Boolean runFlag = true;
	private Session session;
	private Quiz runningQuiz;
	private Vector<Cohort> cohortVector;
	private Vector<Subject> subjectVector;
	private int quizFinishedCounter = 0;
	private int numberOfSubjects = 0;
	private Boolean cleanUnfinishedPeriod;

	public SessionThread(String name, Session session, SessionQueue periodQueue, Boolean continueSessionFlag) {
		super(name);
		this.periodQueue = periodQueue;
		this.session = session;
		this.cleanUnfinishedPeriod = continueSessionFlag;

		start();
	}

	/**
	 * Run Session
	 */
	@Override
	public void run() {

		Vector<SequenceElement> sequenceElementVector = new Vector<SequenceElement>(session.getSequenceElements());
		Collections.sort(sequenceElementVector);
		Vector<Period> periodVector;

		for (SequenceElement sequenceElement : sequenceElementVector) {

			if (!sequenceElement.getDone()) {
				runStateLogger.createOutputMessage("Starting: " + sequenceElement.toString());

				if (sequenceElement.getClass().equals(Pause.class)) {

					Pause pause = (Pause) sequenceElement;
					try {
						startPause(pause);
					} catch (RemoteException e) {
						runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
					}
				}

				if (sequenceElement.getClass().equals(Quiz.class)) {

					runningQuiz = (Quiz) sequenceElement;

					try {
						startQuiz(runningQuiz);
					} catch (RemoteException e) {
						runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
					}

				}

				if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

					TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

					periodVector = new Vector<Period>(treatmentBlock.getPeriods());
					Collections.sort(periodVector);

					for (Period period : periodVector) {

						if (!period.getDone()) {

							if (cleanUnfinishedPeriod) {
								deleteTrials(period);
							}

							try {
								runStateLogger.createOutputMessage("Starting: " + period.toString());
								startPeriod(period);
								period.setDone(true);
								periodManagement.updatePeriod(period);

							} catch (Exception e) {
								runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
							}
						} else {
							runStateLogger.createOutputMessage("Skiping: " + period.toString() + " (ALREADY DONE)");
						}
					}
				}

				sequenceElement.setDone(true);
				try {
					sequenceElementManagement.updateSequenceElement(sequenceElement);
				} catch (StructureManagementException e) {
					runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
				}
				runStateLogger.createOutputMessage("--- --- --- --- --- ---");

			} else {
				runStateLogger.createOutputMessage("Skiping: " + sequenceElement.toString() + " (ALREADY DONE)");
			}
		}

		try {
			messageSender.sendToALL(DefaultInfoScreen.class.getName(), new DefaultInfoScreen.ParamObject("<html><body><h1>End of the experimental session!</h1>Thank you!</body></html>"));
		} catch (RemoteException e) {
			runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
		}

		runStateLogger.createOutputMessage("End of session!");

		// Session finished set DONE!
		session.setDone(true);
		try {
			SessionManagement.getInstance().updateSession(session);
		} catch (StructureManagementException e) {
			runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
		}
	}

	/**
	 * Do a pause.
	 * 
	 * @param pause
	 * @throws RemoteException
	 */
	private void startPause(Pause pause) throws RemoteException {

		messageSender.sendToALLWithDeadLine(DefaultInfoScreen.class.getName(), new DefaultInfoScreen.ParamObject(pause.getMessage()), pause.getTime());

	}

	/**
	 * Starts a quiz.
	 * 
	 * @param quiz
	 * @throws RemoteException
	 */
	private void startQuiz(Quiz quiz) throws RemoteException {

		runFlag = true;
		String screenId = quiz.getQuizFactoryKey();
		// Quiz has no parameters, but parameters should not be null
		messageSender.sendToALL(screenId, null);

		ClientMessage msg = null;

		while (runFlag) {
			msg = periodQueue.pop();
//			System.out.println(msg.toString());
			try {
				processMessage(msg);
			} catch (Exception e) {
				runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
			}
		}

	}

	/**
	 * Starts a period.
	 * 
	 * @param period
	 * @throws Exception
	 */
	private void startPeriod(Period period) throws Exception {

		runFlag = true;
		System.out.println("<<<<<<<" + Thread.currentThread());
		int numberOfGroups = period.getSubjectGroups().size();

		// Start one game for every subject group
		for (Integer index = 0; index < numberOfGroups; index++) {

			SubjectGroup subjectGroup = period.getSubjectGroups().get(index);
			String gameId = subjectGroup.getIdSubjectGroup().toString();
			
			Environment environment = EnvironmentFactory.createEnvironment(period.getTreatment().getEnvironmentFactoryKey());
			Institution<? extends Environment> institution = InstitutionFactory.createInstitution(period.getTreatment().getInstitutionFactoryKey(), environment, subjectGroup.getMemberships(), messageSender, gameId);
			institution.setCurrentTreatment(period.getTreatment());
			institution.setCurrentPeriod(period);
			gameMap.put(gameId, institution);
			institution.startPeriod();

		}

		ClientMessage msg = null;

		// Wait for client messages
		while (runFlag) {
			msg = periodQueue.pop();
//			System.out.println(msg.toString());
			try {
				processMessage(msg);
			} catch (Exception e) {
				runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
			}
		}
	}

	/**
	 * Process ClientMessage
	 * 
	 * @param message
	 * @throws Exception
	 */
	public void processMessage(ClientMessage message) throws Exception {

		// Process client response messages
		if (message.getClass().equals(ClientResponseMessage.class)) {

			ClientResponseMessage clientResponseMessage = (ClientResponseMessage) message;
			int finishedCounter = 0;
			String gameId = clientResponseMessage.getGameId();
			Institution<? extends Environment> institution = gameMap.get(gameId);
			institution.processMessage(clientResponseMessage);

			for (Map.Entry<String, Institution<? extends Environment>> entry : gameMap.entrySet()) {

				Institution<? extends Environment> inst = entry.getValue();

				if (inst.isFinished()) {
					finishedCounter++;
				}
			}

			if (finishedCounter == gameMap.size()) {
				runFlag = false;
			}
		}

		// Process a trial log message
		if (message.getClass().equals(ClientTrialLogMessage.class)) {

			ClientTrialLogMessage logMsg = (ClientTrialLogMessage) message;
			String subjectGroupId = logMsg.getGameId();

			SubjectGroup subjectGroup = subjectGroupManagement.findSubjectGroup(Long.valueOf(subjectGroupId));

			Subject subject = null;

			for (Membership membership : subjectGroup.getMemberships()) {
				if (membership.getSubject().getIdClient().equals(logMsg.getClientId())) {
					subject = membership.getSubject();
				}
			}

			Trial trial = logMsg.getTrial();
			trial.setSubject(subject);
			trial.setSubjectGroup(subjectGroup);
			TrialManagement.getInstance().createNewTrial(trial);
		}

		// Process quiz messages
		if (message.getClass().equals(QuizProtocolMessage.class)) {

			QuizProtocolMessage quizProtocolMessage = (QuizProtocolMessage) message;

			if (cohortVector == null) {

				cohortVector = new Vector<Cohort>();
				cohortVector.addAll(session.getCohorts());
				subjectVector = new Vector<Subject>();

				for (Cohort cohort : cohortVector) {
					numberOfSubjects += cohort.getSize();
					subjectVector.addAll(cohort.getSubjects());
				}
			}

			Protocol protocol = new Protocol();
			protocol.setPassed(quizProtocolMessage.getPassed());
			protocol.setQuiz(runningQuiz);
			protocol.setSolution(quizProtocolMessage.getQuizSolution());
			Subject subject = null;

			for (Subject s : subjectVector) {
				if (s.getIdClient().equals(quizProtocolMessage.getClientId())) {
					subject = s;
				}
			}

			protocol.setSubject(subject);
			ProtocolManagement.getInstance().createNewProtocol(protocol);
			quizFinishedCounter++;

			if (quizFinishedCounter == numberOfSubjects) {
				runFlag = false;
			}
		}

	}

	/**
	 * Deletes trials of a given period.
	 * 
	 * @param period
	 */
	private void deleteTrials(Period period) {

		Vector<SubjectGroup> groups = new Vector<SubjectGroup>(period.getSubjectGroups());

		for (SubjectGroup subjectGroup : groups) {

			Vector<Trial> trials = new Vector<Trial>(subjectGroup.getTrials());
			for (Trial t : trials) {
				try {
					trialManagement.deleteTrial(t.getIdTrial());
				} catch (StructureManagementException e) {
					runStateLogger.createOutputMessage("ERROR: " + e.getMessage());
				}
			}
		}

	}

}
