package edu.kit.exp.client.comunication;

import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JOptionPane;

import edu.kit.exp.client.gui.LoginScreen;
import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.common.IClient;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.PhysioControlCommand;
import edu.kit.exp.common.communication.IncommingMessageQueue;
import edu.kit.exp.common.communication.MessageDeliveryThread;

/**
 * This class implements the functionality provided to the ExpServer! The
 * ExpServer triggers the methods of this class to control the client.
 */
public class ClientImpl implements IClient {

	private IncommingMessageQueue<ServerMessage> messageQueue;
	private static ClientImpl instance;
	private MessageDeliveryThread<ServerMessage> messageDeliveryThread;

	public static ClientImpl getInstance() {
		if (instance == null) {
			instance = new ClientImpl();
		}

		return instance;
	}

	private ClientImpl() {
		messageQueue = ClientCommunicationManager.getInstance().getIncommingMessageQueue();
		setMessageDeliveryThread(new MessageDeliveryThread<ServerMessage>("CLIENT MessageDeliveryThread", messageQueue, ClientCommunicationManager.getInstance().getMessageReceiver()));
	}

	@Override
	public void showGeneralScreen(String globalScreenId, IScreenParamObject parameters) throws RemoteException {
		messageQueue.push(new ServerExperimentMessage(ServerExperimentMessage.SHOW_GENERAL_SCREEN, globalScreenId, parameters, null));
	}

	@Override
	public void tryReconnect(String string) throws RemoteException {
		MainFrame.getInstance().dispose();
		LoginScreen.getInstance().setVisible(true);

		JOptionPane.showMessageDialog(LoginScreen.getInstance(), "Login failed please check your settings and try again!", "Login error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void showScreen(String globalScreenId, IScreenParamObject parameters, String gameId) throws RemoteException {
		messageQueue.push(new ServerExperimentMessage(ServerExperimentMessage.SHOW_SCREEN, globalScreenId, parameters, gameId));
	}

	@Override
	public void showScreenWithDeadLine(String globalScreenId, IScreenParamObject parameters, String gameId, Long showUpDuration) throws RemoteException {
		messageQueue.push(new ServerExperimentMessage(ServerExperimentMessage.SHOW_SCREEN_WITH_DEADLINE, globalScreenId, parameters, gameId, showUpDuration));
	}

	@Override
	public void executePhysioCommand(PhysioControlCommand command, List<String> sensorList) throws RemoteException {
		messageQueue.push(new ServerPhysioCommandMessage(command, sensorList));
	}

	@Override
	public void sendParamObjectUpdate(IScreenParamObject parameter) throws RemoteException {
		messageQueue.push(new ServerParamObjectUpdateMessage(parameter));
	}

	public MessageDeliveryThread<ServerMessage> getMessageDeliveryThread() {
		return messageDeliveryThread;
	}

	public void setMessageDeliveryThread(MessageDeliveryThread<ServerMessage> messageDeliveryThread) {
		this.messageDeliveryThread = messageDeliveryThread;
	}
}