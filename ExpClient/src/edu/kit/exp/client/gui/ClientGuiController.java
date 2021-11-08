package edu.kit.exp.client.gui;

import java.util.Date;

import edu.kit.exp.client.comunication.ClientCommunicationManager;
import edu.kit.exp.client.comunication.ClientMessageSender;
import edu.kit.exp.client.gui.screens.DefaultWaitingScreen;
import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.common.communication.ConnectionException;

/**
 * This controller coordinates all GUI interactions on client side.
 * 
 */
public class ClientGuiController {

	private static ClientGuiController instance;
	private ClientMessageSender messageSender = ClientCommunicationManager.getInstance().getMessageSender();
	private MainFrame mainFrame;
	private Screen currentScreen = null;

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	private int counter = 0;
	private DefaultWaitingScreen defaultWaitingScreen;

	private ClientGuiController() {
		mainFrame = MainFrame.getInstance();
	}

	public static ClientGuiController getInstance() {

		if (instance == null) {
			instance = new ClientGuiController();
		}

		return instance;
	}

	/**
	 * Login on server
	 * 
	 * @param clientId
	 * @param serverIP
	 * @throws ConnectionException
	 */
	public void login(String clientId, String serverIP) throws ConnectionException {

		if (counter < 1) {
			messageSender.registerAtServer(clientId, serverIP);
			counter++;
		} else {
			messageSender.reconnect(clientId);
		}

	}

	/**
	 * This Method sends a message to the server.
	 * 
	 * @param parameters
	 * @param gameId
	 */
	public void sendClientResponse(IScreenParamObject parameters, String gameId, String screenId) {
		Date date = new Date();
		Long timeStamp = date.getTime();
		messageSender.sendMessage(parameters, gameId, screenId, timeStamp);

	}

	/**
	 * This Method sends a message to the server and displays the default wait
	 * screen at this client.
	 * 
	 * @param parameters
	 *            A list of all additional parameters used in the institution.
	 * @param gameId
	 *            The ID of the current game.
	 * @param screenId
	 *            The id of the screen that calls this method.
	 */
	public void sendClientResponseMessageAndWait(IScreenParamObject parameters, String gameId, String screenId) {

		Date date = new Date();
		Long timeStamp = date.getTime();

		if (defaultWaitingScreen == null) {
			defaultWaitingScreen = new DefaultWaitingScreen(null, new DefaultWaitingScreen.ParamObject(), DefaultWaitingScreen.class.getName(), 0L);
		}

		messageSender.sendMessage(parameters, gameId, screenId, timeStamp);
		// showScreen(defaultWaitingScreen);

	}

	/**
	 * Shows a screen at this client.
	 * 
	 * @param screen
	 */
	public void showScreen(Screen screen) {

		currentScreen = screen;
		mainFrame.showScreen(currentScreen);
	}

	/**
	 * This method is user to send trialLogMessages to the server. With this
	 * methods events on client side, which are not observable by the server can
	 * be logged. On server side a trial will be created for the corresponding
	 * subject and subject group, with the given parameters.
	 * 
	 * @param gameId
	 *            The ID of the current game.
	 * @param event
	 *            The event you want to log.
	 * @param screenName
	 *            The screen corresponding to that event.
	 * @param value
	 *            The value of that event.
	 * @param clientTimeStamp
	 *            the time the event happence on client side.
	 */
	public void sendTrialLogMessage(String gameId, String event, String screenName, String value, Long clientTimeStamp) {
		messageSender.sendTrialLogMessage(gameId, event, screenName, value, clientTimeStamp);
	}

	/**
	 * Logs trial information for the current screenName, gameid, and time, if
	 * available.
	 * 
	 * @see #sendTrialLogMessage(String, String, String, String, Long)
	 */
	public void sendTrialLogMessage(String event, String value) {
		if (currentScreen != null) {
			sendTrialLogMessage(currentScreen.getGameId(), event, currentScreen.getScreenId(), value, new Date().getTime());
		} else {
			sendTrialLogMessage("[unknown gameId]", event, "[unknown screenName]", value, new Date().getTime());
		}
	}

	/**
	 * Shows a screen for the time span defined in screen.getShowUpTime(). After
	 * that the a waiting screen is shown automatically.
	 * 
	 * @param screen
	 */
	public void showScreenWithDeadLine(Screen screen) {

		if (defaultWaitingScreen == null) {
			defaultWaitingScreen = new DefaultWaitingScreen(null, new DefaultWaitingScreen.ParamObject(), DefaultWaitingScreen.class.getName(), 0L);
		}

		showScreen(screen);

		try {
			Thread.sleep(screen.getShowUpTime());
		} catch (InterruptedException e) {
			// Do nothing... Program will continue
		}
		showScreen(defaultWaitingScreen);
	}

	/**
	 * Sends a quiz protocol to the server and shows a waiting screen
	 * 
	 * @param passed
	 * @param quizSolution
	 */
	public void sendQuizProtocolAndWait(Boolean passed, String quizSolution) {

		if (defaultWaitingScreen == null) {
			defaultWaitingScreen = new DefaultWaitingScreen(null, new DefaultWaitingScreen.ParamObject(), DefaultWaitingScreen.class.getName(), 0L);
		}

		messageSender.sendQuizProtocol(passed, quizSolution);
		showScreen(defaultWaitingScreen);
	}

	/**
	 * 
	 * @param param
	 */
	public void processParamObjectUpdate(IScreenParamObject param) {
		if (currentScreen != null) {
			currentScreen.processParamObjectUpdate(param);
		}
	}
}
