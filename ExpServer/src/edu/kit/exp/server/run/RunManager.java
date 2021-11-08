package edu.kit.exp.server.run;

import java.rmi.RemoteException;

import edu.kit.exp.server.communication.ClientMessage;
import edu.kit.exp.common.communication.ConnectionException;
import edu.kit.exp.server.communication.ServerCommunicationManager;
import edu.kit.exp.common.communication.MessageDeliveryThread;
import edu.kit.exp.server.communication.ServerImpl;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * This Class handles the run of a session.
 * 
 */
public class RunManager {

	private static RunManager instance;
	private SessionCreator sessionCreator;
	private SubjectRegistration subjectRegistration;
	private SessionManagement sessionManagement;
	private Session currentSession;
	private Boolean continueSessionFlag;

	public static RunManager getInstance() {

		if (instance == null) {
			instance = new RunManager();
		}

		return instance;
	}

	private RunManager() {
		sessionCreator = SessionCreator.getInstance();
		subjectRegistration = SubjectRegistration.getInstance();
		sessionManagement = SessionManagement.getInstance();
	}

	/* Methods */

	/**
	 * Main method to start a Session.
	 * 
	 * @param session
	 * @param continueSession
	 * @throws SessionRunException
	 * @throws ExistingDataException
	 * @throws ConnectionException
	 */
	public void initializeSession(Session session, Boolean continueSession) throws SessionRunException, ExistingDataException, ConnectionException {

		this.continueSessionFlag = continueSession;

		if (session.getStarted() && continueSession == false) {
			throw new ExistingDataException("Session was started before!");
		}

		if (continueSession) {
			RunStateLogger.getInstance().setContinueSessionFlag(true);
		}

		// Interrupted session has to be continued
		if (session.getStarted() == false) {

			// Reset doneFlag on SequenceElements
			try {
				session = sessionCreator.resetTreatmentElementFlags(session);
			} catch (StructureManagementException e) {
				throw new SessionRunException(e.getMessage());
			}

			// Create new session data
			sessionCreator.checkIfRunConditionsMet(session);
			session = sessionCreator.createSubjects(session);
			session = sessionCreator.matchPeriodsToTreatments(session);
			session = sessionCreator.defineSubjectGroups(session);
			System.out.println("NEUEN DATEN");
		}

		System.out.println("KEINE NEUEN DATEN");

		RunStateLogger.getInstance().createOutputMessage("Session successfully initialised!");
		startNetworkServer(session);
		RunStateLogger.getInstance().createOutputMessage("Server ready and waiting for Client connections...");

		currentSession = session;

	}

	/**
	 * Call this method to enables the server application to communicate via the
	 * network and enables the clients to connect to the server. This method
	 * starts the RMI interface, a queue for incoming messages and the
	 * MessageDeliveryThread.
	 * 
	 * @param session
	 * @throws ConnectionException
	 */
	private void startNetworkServer(Session session) throws ConnectionException {

		try {

			// if (session.getStarted() == false) {
			subjectRegistration.prepareClientToSubjectMatching(session);
			// }

			// Needed for RMI settings
			System.setProperty("java.security.policy", "file:./java.policy");

			// The messageDeliveryThread will rout all client messages to its
			// destination objects. It is implemented as daemon thread so it
			// will terminate at
			// application end.
			@SuppressWarnings("unused")
			MessageDeliveryThread<ClientMessage> messageDeliveryThread = new MessageDeliveryThread<ClientMessage>("QueueThread"
										, ServerCommunicationManager.getInstance().getIncommingMessageQueue()
										, ServerCommunicationManager.getInstance().getMessageReceiver());

			// Start the RMI Service of the server
			@SuppressWarnings("unused")
			ServerImpl serverImpl = ServerImpl.getInstance();
			System.out.println(Thread.currentThread().toString());

		} catch (RandomGeneratorException e) {
			throw new ConnectionException(e.getMessage());
		} catch (RemoteException e) {
			throw new ConnectionException("Can not start network server. " + e.getMessage());
		}
	}

	/**
	 * This methods starts a session by starting a new SessionThread.
	 * 
	 * @throws SessionRunException
	 */
	public void startSession() throws SessionRunException {

		System.out.println(Thread.currentThread().toString());

		// Make sure to have the most actual data and set session started!
		try {
			currentSession = sessionManagement.findSession(currentSession.getIdSession());
			currentSession.setStarted(true);
			currentSession = sessionManagement.updateSession(currentSession);
		} catch (StructureManagementException e) {
			throw new SessionRunException(e.getMessage());
		}

		/* Start session thread */		
		@SuppressWarnings("unused")
		SessionThread sessionThread = new SessionThread("PeriodThread", currentSession, SessionQueue.getInstance(), continueSessionFlag);
		RunStateLogger.getInstance().createOutputMessage("Session sucsessfully started!");

	}

}
