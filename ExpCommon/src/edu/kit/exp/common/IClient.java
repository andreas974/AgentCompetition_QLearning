package edu.kit.exp.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This Interface defines all Methods the client provides to the server.
 * 
 */
public interface IClient extends Remote {

	/**
	 * Request the client to try a reconnect.
	 * 
	 * @param string
	 * @throws RemoteException
	 */
	public void tryReconnect(String string) throws RemoteException;

	/**
	 * This method shows a screen at the client. The gameId allows the server to
	 * route this message to the right game. This method should be used by
	 * default to play periods.
	 * 
	 * @param globalScreenId
	 *            The screen to be shon at the client.
	 * @param parameter
	 *            Custom parameters for that screen.
	 * @param gameId
	 *            ID of the running game, the screen belongs to.
	 * @throws RemoteException
	 */
	public void showScreen(String globalScreenId, IScreenParamObject parameter, String gameId) throws RemoteException;

	/**
	 * This method shows the defined screen for the defined showUpDuration and
	 * then switches to default waiting screen!
	 * 
	 * @param globalScreenId
	 *            The screen to be shon at the client.
	 * @param parameter
	 *            Custom parameters for that screen.
	 * @param gameId
	 *            ID of the running game, the screen belongs to.
	 * @param showUpTime
	 *            The period of time the screen will be visible at the client.
	 *            In milliseconds!
	 * @throws RemoteException
	 */
	public void showScreenWithDeadLine(String globalScreenId, IScreenParamObject parameter, String gameId, Long showUpTime) throws RemoteException;

	/**
	 * This method shows a screen at the client. There is NO gameId, so this
	 * method should only be used for general client interactions like login. Do
	 * NOT use this method to show screens in a running session!
	 * 
	 * @param globalScreenId
	 *            The screen to be shon at the client.
	 * @param parameter
	 *            Custom parameters for that screen.
	 */
	public void showGeneralScreen(String globalScreenId, IScreenParamObject parameter) throws RemoteException;

	/**
	 * 
	 * @param parameter
	 * @throws RemoteException
	 */
	public void sendParamObjectUpdate(IScreenParamObject parameter) throws RemoteException;
	
	/**
	 * 
	 * 
	 * @param command
	 * @throws RemoteException
	 */
	public void executePhysioCommand(PhysioControlCommand command, List <String> sensorList) throws RemoteException;
}
