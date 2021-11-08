package edu.kit.exp.client.comunication;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.swing.JOptionPane;

import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.common.Constants;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.IServer;
import edu.kit.exp.common.communication.ConnectionException;
import edu.kit.exp.common.communication.MessageSender;

public class ClientMessageSender extends MessageSender {
	private IServer serverRemoteObject;
	private String clientId;
	private ClientImpl clientRemoteObject;
	
	ClientMessageSender(){
		
	}
	
	/**
	 * Connect this client to the ExpServer. The Server can be found with name
	 * <code>Constants.SERVER_RMI_OBJECT_NAME</code> and port
	 * <code>Constants.SERVER_PORT</code>
	 * 
	 * @param serverIP
	 * 
	 * @throws ConnectionException
	 */
	public void registerAtServer(String clientId, String serverIP) throws ConnectionException {

		System.setProperty("java.security.policy", "file:./java.policy");

		this.clientId = clientId;

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {

			// Find server
			serverRemoteObject = (IServer) Naming.lookup("rmi://" + serverIP + ":" + Constants.SERVER_PORT + "/" + Constants.SERVER_RMI_OBJECT_NAME);

			// Creat remote control object of this client for server
			clientRemoteObject = ClientImpl.getInstance();

			// Register at server and send remote handler of client
			UnicastRemoteObject.exportObject(clientRemoteObject, 0);
			serverRemoteObject.registerClient(clientRemoteObject, clientId);

		} catch (Exception e) {

			ConnectionException ex = new ConnectionException(e.getMessage());
			throw ex;
		}
	}

	/**
	 * Send a reconnect message after a failed login.
	 * 
	 * @param clientId
	 * @throws ConnectionException
	 */
	public void reconnect(String clientId) throws ConnectionException {
		try {
			serverRemoteObject.registerClient(clientRemoteObject, clientId);
		} catch (RemoteException e) {
			ConnectionException ex = new ConnectionException(e.getMessage());
			throw ex;
		}

	}

	/**
	 * Sends a message to the server.
	 * 
	 * @param parameters
	 *            A list of parameters you want to send to the server.
	 * @param gameId
	 *            ID of the running game, whose institution triggered the
	 *            client.
	 * @param screenId
	 *            The global screen id has to be given for a complete trial
	 *            entry at server side.
	 * @param clientTimeStamp
	 *            The time at which a screen calls the guiController (i.e. the
	 *            time a button is pressed) in milliseconds
	 */
	public void sendMessage(IScreenParamObject parameters, String gameId, String screenId, Long clientTimeStamp) {

		try {
			serverRemoteObject.receiveClientResponseMessage(clientId, parameters, gameId, screenId, clientTimeStamp);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Creates a trial on server side.
	 * 
	 * @param gameId
	 *            ID of the running game, whose institution triggered the
	 *            client.
	 * @param event
	 *            The name of the event.
	 * @param screenId
	 *            The global screen id has to be given for a complete trial
	 *            entry at server side.
	 * @param value
	 *            The value of the event.
	 * @param clientTimeStamp
	 *            The time at which a screen calls the guiController (i.e. the
	 *            time a button is pressed) in milliseconds
	 */
	public void sendTrialLogMessage(String gameId, String event, String screenId, String value, Long clientTimeStamp) {

		try {
			serverRemoteObject.receiveTrialLogMessage(clientId, gameId, event, screenId, value, clientTimeStamp);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Creates a quiz protocol on server side.
	 * 
	 * @param passed
	 * @param quizSolution
	 */
	public void sendQuizProtocol(Boolean passed, String quizSolution) {

		try {
			serverRemoteObject.receiveQuizProtocol(clientId, passed, quizSolution);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	/**
	 * Sends the current screen shown at the client to the server
	 * 
	 * @param screenName
	 */
	public void sendCurrentClientScreen(String screenName){
		try {
			serverRemoteObject.receiveCurrentClientScreen(clientId, screenName);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}		
	}
	
	public void sendPhysioData(Long timestamp, HashMap<String, Double> physioData){
		try {
			serverRemoteObject.receivePhysioData(clientId, timestamp, physioData);
		} catch (RemoteException e) {
			JOptionPane.showInternalMessageDialog(MainFrame.getInstance(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}		
	}
}
