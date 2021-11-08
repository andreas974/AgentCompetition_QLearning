package edu.kit.exp.server.run;

import java.util.Observable;
import java.util.Observer;

import org.joda.time.DateTime;

import edu.kit.exp.common.Constants;

/**
 * This thread save class indicates the state of a session. 
 * The RunTab GUI observes this class to inform the experimenter about all events in a session run, i.e. errors or progress messages
 *
 */
public class RunStateLogger extends Observable {

	private static RunStateLogger instance;
	private String stateMessage;
	private boolean allSubjectsConnected = false;
	private boolean continueSessionFlag = false;

	private RunStateLogger() {

	}

	/**
	 * Returns the only instance of this class.
	 * 
	 * @return
	 */
	public synchronized static RunStateLogger getInstance() {

		if (instance == null) {
			instance = new RunStateLogger();
		}

		return instance;
	}

	/**
	 * Add an observer to this class.
	 */
	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
	}

	public synchronized void  createOutputMessage(String message) {

		DateTime dt = new DateTime();
		stateMessage = dt.toString(Constants.TIME_STAMP_FORMAT)+": "+message + System.lineSeparator();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(stateMessage);
		}

	}

	public synchronized  boolean areAllSubjectsConnected() {
		return allSubjectsConnected;
	}

	public void setAllSubjectsConnected(boolean allSubjectsConnected) {
		this.allSubjectsConnected = allSubjectsConnected;

		if (allSubjectsConnected) {			
			createOutputMessage("All Subjects connected! Press continue to start session.");					
		}
	}

	public synchronized boolean getAllSubjectsConnected() {
		return allSubjectsConnected;
	}

	public synchronized boolean getContinueSessionFlag() {
		
		return continueSessionFlag ;
	}

	public synchronized void setContinueSessionFlag(boolean continueSessionFlag) {
		this.continueSessionFlag = continueSessionFlag;
	}
	
	

}
