package edu.kit.exp.client.gui.screens;

import edu.kit.exp.client.comunication.ClientCommunicationManager;
import edu.kit.exp.common.IScreenParamObject;

/**
 * Class to create screens
 * 
 */
public class ScreenFactory {

	private ScreenFactory() {

	}

	/**
	 * This factory method creates an instance of a screen according to the
	 * given screenId.
	 * 
	 * @param screenId
	 *            screenId
	 * @param parameters
	 *            parameters for the screen.
	 * @param gameId
	 *            gameId
	 * @param showUpTime
	 *            in milliseconds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Screen> T createScreen(String screenId, IScreenParamObject parameters, String gameId, Long showUpTime) {
        if (screenId.equals("")){
        	screenId = DefaultInfoScreen.class.getName();
        }
		
		try
        {
			Class<T> newScreen = (Class<T>) Class.forName(screenId);
			ClientCommunicationManager.getInstance().getMessageSender().sendCurrentClientScreen(newScreen.getSimpleName());
            //return (T)(newScreen.getConstructor(String.class, ArrayList.class, String.class, Long.class).newInstance(gameId, parameters, screenId, showUpTime));
			return (T)(newScreen.getConstructors()[0].newInstance(gameId, parameters, screenId, showUpTime));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
	}
}
