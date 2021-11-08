package edu.kit.exp.client.gui.screens;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.kit.exp.client.gui.ClientGuiController;
import edu.kit.exp.client.gui.MainFrame;
import edu.kit.exp.common.IScreenParamObject;

/**
 * This abstract class represents all screens that can be displayed at client
 * side. All new/custom screens have to extend this class.
 * 
 */
public abstract class Screen extends JPanel {

	public static class ParamObject implements IScreenParamObject {
		private static final long serialVersionUID = 6756327287510841042L;

		private String logTextEnter;
		private String logTextExit;
		private String logValueEnter;
		private String logValueExit;
		private Long logTimeEnter;

		public ParamObject() {
			this.logTextEnter = "EVENT_SCREEN_ENTER";
			this.logTextExit = "EVENT_SCREEN_EXIT";
			this.logValueEnter = null;
			this.logValueExit = null;
			this.logTimeEnter = 0L;
		}

		public String getLogTextEnter() {
			return logTextEnter;
		}

		public void setLogTextEnter(String logTextEnter) {
			this.logTextEnter = logTextEnter;
		}

		public String getLogTextExit() {
			return logTextExit;
		}

		public void setLogTextExit(String logTextExit) {
			this.logTextExit = logTextExit;
		}

		public String getLogValueEnter() {
			return logValueEnter;
		}

		public void setLogValueEnter(String logValueEnter) {
			this.logValueEnter = logValueEnter;
		}

		public String getLogValueExit() {
			return logValueExit;
		}

		public void setLogValueExit(String logValueExit) {
			this.logValueExit = logValueExit;
		}

		public Long getLogTimeEnter() {
			return logTimeEnter;
		}

		public void setLogTimeEnter(Long logTimeEnter) {
			this.logTimeEnter = logTimeEnter;
		}
	}

	public static class ResponseObject implements IScreenParamObject {
		private static final long serialVersionUID = -4251351589318484239L;

	}

	private static final long serialVersionUID = 7881637153570029242L;
	protected String gameId;
	protected MainFrame mainFrame;
	protected ClientGuiController guiController;
	protected ParamObject parameter;
	protected String screenId;
	protected Boolean logDisplayEvent = false;
	protected Long showUpTime = 0l;

	/**
	 * Creates an instance of screen.
	 * 
	 * @param gameId
	 *            ID of the running game, whose institution triggered that
	 *            screen to be shown at the client.
	 * @param parameter
	 *            A List of all parameters used in this screen. i.e. text.
	 * @param screenId
	 *            The global screen id has to be given for a complete trial
	 *            entry at server side.
	 * @param showUpTime2
	 */
	public Screen(String gameId, ParamObject parameter, String screenId, Long showUpTime) {
		this.mainFrame = MainFrame.getInstance();
		this.guiController = ClientGuiController.getInstance();
		this.gameId = gameId;
		this.parameter = parameter == null ? new ParamObject() : parameter;
		this.screenId = screenId;
		this.showUpTime = showUpTime;
	}

	public JButton getDefaultButton() {
		return mainFrame.getRootPane().getDefaultButton();
	}

	public void setDefaultButton(JButton screenDefaultButton) {
		mainFrame.getRootPane().setDefaultButton(screenDefaultButton);
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public ParamObject getParameter() {
		return parameter;
	}

	public void setParameter(ParamObject parameter) {
		this.parameter = parameter;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public Boolean getLogDisplayEvent() {
		return logDisplayEvent;
	}

	public Long getShowUpTime() {
		return showUpTime;
	}

	public void setShowUpTime(Long showUpTime) {
		this.showUpTime = showUpTime;
	}

	public String getLogTextEnter() {
		return parameter.getLogTextEnter();
	}

	public String getLogTextExit() {
		return parameter.getLogTextExit();
	}

	public String getLogValueEnter() {
		return parameter.getLogTextEnter();
	}

	public String getLogValueExit() {
		return parameter.getLogValueExit();
	}

	/**
	 * Sends a ResponseObject back to the server.
	 * 
	 * @param response
	 */
	public void sendResponse(ResponseObject response) {
		guiController.sendClientResponse(response, getGameId(), getScreenId());
	}

	/**
	 * Sends a ResponseObject back to the server and shows the default waiting
	 * screen.
	 * 
	 * @param response
	 */
	public void sendResponseAndWait(ResponseObject response) {
		guiController.sendClientResponseMessageAndWait(response, getGameId(), getScreenId());
	}

	/**
	 * If true, the time at that the screen is visible will be logged as trial
	 * with SCREEN VISIBLE EVENT.
	 * 
	 * @param logDisplayEvent
	 */
	public void setLogDisplayEvent(Boolean logDisplayEvent) {
		this.logDisplayEvent = logDisplayEvent;
	}

	/**
	 * Is called when an updated ParamObject is received from the server.
	 * 
	 * @param newParamObject
	 */
	public void processParamObjectUpdate(IScreenParamObject newParamObject) {

	}
	
	/**
	 * Is called before screen is replaced by another screen.
	 */
	public void extiScreen() {

	}
}
