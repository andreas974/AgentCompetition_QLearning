package edu.kit.exp.client.comunication;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.client.gui.screens.ScreenFactory;
import edu.kit.exp.client.physio.ClientPhysioManager;
import edu.kit.exp.common.communication.MessageReceiver;

/**
 * Class for processing incoming server messages.
 * 
 */
public class ClientMessageReceiver extends MessageReceiver<ServerMessage> {

	private ClientGuiController clientGuiController;

	ClientMessageReceiver() {
		clientGuiController = ClientGuiController.getInstance();
	}

	/**
	 * This method processes a server message dependent on it´s type.
	 * 
	 * @param msg
	 */
	@Override
	public void routeMessage(ServerMessage newMsg) {

		if (newMsg.getClass().equals(ServerParamObjectUpdateMessage.class)) {
			ServerParamObjectUpdateMessage msg = (ServerParamObjectUpdateMessage) newMsg;
			clientGuiController.processParamObjectUpdate(msg.getParameterObjectUpdate());
		}

		else if (newMsg.getClass().equals(ServerPhysioCommandMessage.class)) {
			ServerPhysioCommandMessage msg = (ServerPhysioCommandMessage) newMsg;
			ClientPhysioManager.getInstance().processCommand(msg.getCommand(), msg.getSensorList());
		}

		else if (newMsg.getClass().equals(ServerExperimentMessage.class)) {
			ServerExperimentMessage msg = (ServerExperimentMessage) newMsg;

			if (msg.getType().equals(ServerExperimentMessage.SHOW_SCREEN)) {
				Screen screen = ScreenFactory.createScreen(msg.getGlobalScreenId(), msg.getParameters(), msg.getGameId(), msg.getShowUpTime());
				clientGuiController.showScreen(screen);
			}

			else if (msg.getType().equals(ServerExperimentMessage.SHOW_GENERAL_SCREEN)) {
				Screen screen = ScreenFactory.createScreen(msg.getGlobalScreenId(), msg.getParameters(), null, msg.getShowUpTime());
				clientGuiController.showScreen(screen);
			}

			else if (msg.getType().equals(ServerExperimentMessage.SHOW_SCREEN_WITH_DEADLINE)) {
				Screen screen = ScreenFactory.createScreen(msg.getGlobalScreenId(), msg.getParameters(), msg.getGameId(), msg.getShowUpTime());
				clientGuiController.showScreenWithDeadLine(screen);
			}
		}
	}
}
