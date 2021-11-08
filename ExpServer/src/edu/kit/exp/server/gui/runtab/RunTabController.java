package edu.kit.exp.server.gui.runtab;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import edu.kit.exp.server.gui.mainframe.MainFrame;
import edu.kit.exp.server.run.RunManager;
import edu.kit.exp.server.run.RunStateLogger;
import edu.kit.exp.server.run.SessionRunException;

/**
 * Run tab controller class
 *
 */
public class RunTabController extends Observable implements Observer {

	private static RunTabController instance;
	private RunManager runManager;
	private RunStateLogger runStateLogger;
	private String stateMessage;

	private RunTabController() {
		runManager = RunManager.getInstance();
		runStateLogger = RunStateLogger.getInstance();
		
		runStateLogger.addObserver(this);
	}

	/**
	 * Returns the only instance of this class.
	 * 
	 * @return
	 */
	public static RunTabController getInstance() {
		if (instance == null){
			instance = new RunTabController();
		}
		return instance;
	}

	/**
	 * Add an observer to this controller.
	 */
	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
	}

	public void appendMessage(String message) {

		stateMessage = message;

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(stateMessage);
		}

	}

	@Override
	public void update(Observable o, Object arg) {

		stateMessage = (String) arg;

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(stateMessage);
		}
	}

	public void startSession() throws SessionRunException {

		if (RunStateLogger.getInstance().getAllSubjectsConnected()) {
			runManager.startSession();
		} else {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "Session can not be started until all subjects are connected!", "Please Wait", JOptionPane.WARNING_MESSAGE);
		}

	}

}
