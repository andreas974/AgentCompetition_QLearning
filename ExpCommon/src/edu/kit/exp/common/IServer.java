package edu.kit.exp.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * This Interface defines all Methods the server provides to the clients.
 *
 */
public interface IServer extends Remote {

	/**
	 * Login at server.
	 * 
	 * @param client
	 *            The client remote handler object, which is used by the server
	 *            to communicate with the client.
	 * @param clientId
	 *            The ID of the client (entered at login)
	 * @throws RemoteException
	 */
	public void registerClient(IClient client, String clientId) throws RemoteException;

	/**
	 * Clients can send a response messages to the server by using this method.
	 * 
	 * @param clientId
	 *            The ID of the client (entered at login)
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
	 * @throws RemoteException
	 *             If connection failed.
	 */
	public void receiveClientResponseMessage(String clientId, IScreenParamObject parameters, String gameId, String screenId, Long clientTimeStamp) throws RemoteException;

	/**
	 * Creates a trial.
	 * 
	 * @param clientId
	 *            The ID of the client (entered at login)
	 * @param gameId
	 *            ID of the running game, whose institution triggered the
	 *            client.
	 * @param event
	 *            The event you want to log.
	 * @param screenName
	 *            The global screen id has to be given for a complete trial
	 *            entry at server side.
	 * @param value
	 *            The value of the event.
	 * @param clientTimeStamp
	 *            The time at which a screen calls the guiController (i.e. the
	 *            time a button is pressed) in milliseconds
	 * @throws RemoteException
	 */
	public void receiveTrialLogMessage(String clientId, String gameId, String event, String screenName, String value, Long clientTimeStamp) throws RemoteException;

	/**
	 * Creates and processes a quiz protocol.
	 * 
	 * @param clientId The ID of the client (entered at login)
	 * @param passed True if subject has passed the quiz.
	 * @param quizSolution The answers of the subject as CSV-Sting.
	 * @throws RemoteException
	 */
	public void receiveQuizProtocol(String clientId, Boolean passed, String quizSolution) throws RemoteException;

	/**
	 * Receives the current screen name shown at a client.
	 * 
	 * @param clientId
	 * @param screenName
	 * @throws RemoteException
	 */
	public void receiveCurrentClientScreen(String clientId, String screenName) throws RemoteException;
	
	/**
	 * Receives physio data from client.
	 * 
	 * @param clientId
	 * @param data
	 * @throws RemoteException
	 */
	public void receivePhysioData(String clientId, Long timstamp, HashMap<String, Double> physioData) throws RemoteException;
}
