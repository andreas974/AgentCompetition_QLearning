package edu.kit.exp.server.communication;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;

import edu.kit.exp.common.Constants;
import edu.kit.exp.common.IClient;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.IServer;
import edu.kit.exp.common.communication.IncommingMessageQueue;

/**
 * Implementation of the Server interface. This class provides methods for
 * clients to communicate with the server.
 * 
 */
public class ServerImpl extends UnicastRemoteObject implements IServer {

	private IncommingMessageQueue<ClientMessage> messageQueue;
	private static final long serialVersionUID = 105295106180455257L;

	/* Singleton Pattern */
	private static ServerImpl instance = null;

	private ServerImpl() throws RemoteException {
		messageQueue = ServerCommunicationManager.getInstance().getIncommingMessageQueue();
		registerServer();
	}

	public static ServerImpl getInstance() throws RemoteException {

		if (instance == null) {
			instance = new ServerImpl();
		}

		return instance;
	}

	/**
	 * Call this method to register this server. It´s registered with Name and
	 * Port.
	 * 
	 * @see For Name: <code>Constants.SERVER_RMI_OBJECT_NAME</code> and
	 *      <code>Constants.SERVER_PORT</code>
	 * 
	 * @throws RemoteException
	 */
	private synchronized void registerServer() throws RemoteException {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		Registry registry = LocateRegistry.getRegistry(Constants.SERVER_PORT);

		boolean bound = false;

		for (int i = 0; !bound && i < 2; i++) {

			try {
				registry.rebind(Constants.SERVER_RMI_OBJECT_NAME, this);
				bound = true;
				System.out.println(Constants.SERVER_RMI_OBJECT_NAME + " bound to registry, port " + Constants.SERVER_PORT + ".");
			} catch (RemoteException e) {
				System.out.println("Rebinding " + Constants.SERVER_RMI_OBJECT_NAME + " failed, retrying ...");
				registry = LocateRegistry.createRegistry(Constants.SERVER_PORT);
				System.out.println("Registry started on port " + Constants.SERVER_PORT + ".");
			}
		}

		System.out.println("Server ready.");

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.exp.common.IServer#registerClient(edu.kit.exp.common.IClient, java.lang.String)
	 */
	@Override
	public synchronized void registerClient(IClient clientRemoteObject, String clientId) throws RemoteException {

//		System.out.println(clientRemoteObject.toString() + ", " + clientId);

		ClientRegistrationMessage clientRegistrationMessage = new ClientRegistrationMessage(clientId, clientRemoteObject);
		messageQueue.push(clientRegistrationMessage);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.exp.common.IServer#receiveClientMessage(java.lang.String, IScreenParamObject, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void receiveClientResponseMessage(String clientId, IScreenParamObject parameters, String gameId, String screenId, Long clientTimeStamp) {

		Date date = new Date();
		Long serverTimeStamp = date.getTime();		

//		System.out.println("SERVER: Received Message!!!" + parameters.toString());

		ClientResponseMessage clientResponseMessage = new ClientResponseMessage(clientId, parameters, gameId, screenId, clientTimeStamp, serverTimeStamp);
		messageQueue.push(clientResponseMessage);	
		
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.exp.common.IServer#receiveTrialLogMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void receiveTrialLogMessage(String clientId, String gameId, String event, String screenName, String value, Long clientTimeStamp) throws RemoteException {
		
		Date date = new Date();
		Long serverTimeStamp = date.getTime();
		
		ClientTrialLogMessage clientTrialLogMessage = new ClientTrialLogMessage(clientId, gameId, event, screenName, value, clientTimeStamp,serverTimeStamp);
		messageQueue.push(clientTrialLogMessage);
		
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.exp.common.IServer#receiveQuizProtocol(java.lang.String, java.lang.Boolean, java.lang.String)
	 */
	@Override
	public void receiveQuizProtocol(String clientId, Boolean passed, String quizSolution) {
		
		QuizProtocolMessage quizProtocolMessage = new QuizProtocolMessage (clientId, passed, quizSolution);
		messageQueue.push(quizProtocolMessage);
		
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.exp.common.IServer#receiveCurrentClientScreen(String clientId, String screenName) 
	 */
	@Override
	public void receiveCurrentClientScreen(String clientId, String screenName) throws RemoteException {
		ClientState.getClient(clientId).setCurrentScreen(screenName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.exp.common.IServer#receivePhysioData(java.lang.String, java.util.HashMap)
	 */
	@Override
	public void receivePhysioData(String clientId, Long timstamp, HashMap<String, Double> physioData) throws RemoteException {
		ClientPhysioDataMessage clientPhysioMessage = new ClientPhysioDataMessage(clientId, timstamp, physioData);
		messageQueue.push(clientPhysioMessage);
	}
}
