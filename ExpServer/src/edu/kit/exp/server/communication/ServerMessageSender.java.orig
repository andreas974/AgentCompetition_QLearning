package edu.kit.exp.server.communication;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map.Entry;

import edu.kit.exp.client.gui.screens.DefaultWelcomeScreen;
import edu.kit.exp.common.IClient;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.PhysioControlCommand;
import edu.kit.exp.common.communication.MessageSender;
import edu.kit.exp.server.jpa.entity.Subject;

/**
 * This Class provides functionalities to send messages to clients.
 * 
 */
public class ServerMessageSender extends MessageSender {

	ServerMessageSender() {

	}

	/**
	 * A client has to be registered to sent messages to it.
	 * 
	 * @param clientRegistrationMessage
	 */
	public synchronized void registerClient(ClientRegistrationMessage clientRegistrationMessage) {
		String clientId = clientRegistrationMessage.getClientId();
		IClient clientRemoteObject = clientRegistrationMessage.getClientRemoteObject();
		ClientState.getClient(clientId).setClientRemoteObject(clientRemoteObject);
	}

	public synchronized void sendWelcomeMessage(Subject updatedSubject) throws RemoteException {
		IClient clientRemoteObject = ClientState.getClient(updatedSubject.getIdClient()).getClientRemoteObject();
		clientRemoteObject.showGeneralScreen(DefaultWelcomeScreen.class.getName(), new DefaultWelcomeScreen.ParamObject());
	}

	public synchronized void sendLoginError(IClient clientRemoteObject) {
		try {
			clientRemoteObject.tryReconnect("Login error! Please check your clientId");
		} catch (RemoteException e) {
			// Unknown client is not reachable ... ignore
		}
	}

	public synchronized void sendShowScreenMessage(String clientId, String globalScreenId, IScreenParamObject parameter, String gameId) throws RemoteException {
		IClient clientRemoteObject = ClientState.getClient(clientId).getClientRemoteObject();
		clientRemoteObject.showScreen(globalScreenId, parameter, gameId);
	}

	public synchronized void sendShowScreenMessage(String clientId, String screenId, IScreenParamObject parameter, String gameId, Long showUpTime) throws RemoteException {
		IClient clientRemoteObject = ClientState.getClient(clientId).getClientRemoteObject();
		clientRemoteObject.showScreenWithDeadLine(screenId, parameter, gameId, showUpTime);
	}

	public synchronized void sendToALLWithDeadLine(String screenId, IScreenParamObject parameter, Long showUpTime) throws RemoteException {
		for (Entry<String, ClientState> entry : ClientState.getClients()) {
			IClient clientRemoteObject = entry.getValue().getClientRemoteObject();
			clientRemoteObject.showScreenWithDeadLine(screenId, parameter, "NoGameID", showUpTime);
		}
	}

	public synchronized void sendToALL(String screenId, IScreenParamObject parameters) throws RemoteException {
		for (Entry<String, ClientState> entry : ClientState.getClients()) {
			IClient clientRemoteObject = entry.getValue().getClientRemoteObject();
			clientRemoteObject.showGeneralScreen(screenId, parameters);
		}
	}

	public synchronized void sendParamObjectUpdate(String clientId, IScreenParamObject parameter) throws RemoteException {
		IClient clientRemoteObject = ClientState.getClient(clientId).getClientRemoteObject();
		clientRemoteObject.sendParamObjectUpdate(parameter);
	}

	public synchronized void sendPhysioCommandToALL(PhysioControlCommand command, List<String> sensorList) throws RemoteException {
		for (Entry<String, ClientState> entry : ClientState.getClients()) {
			IClient clientRemoteObject = entry.getValue().getClientRemoteObject();
			clientRemoteObject.executePhysioCommand(command, sensorList);
		}
	}

	public synchronized void sendPhysioCommandToClient(String clientId, PhysioControlCommand command, List<String> sensorList) throws RemoteException {

		IClient clientRemoteObject = ClientState.getClient(clientId).getClientRemoteObject();
		clientRemoteObject.executePhysioCommand(command, sensorList);
	}
}
