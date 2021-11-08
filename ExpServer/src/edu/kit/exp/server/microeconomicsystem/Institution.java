package edu.kit.exp.server.microeconomicsystem;

import java.rmi.RemoteException;
import java.util.List;

import edu.kit.exp.client.gui.screens.Screen;
import edu.kit.exp.common.IScreenParamObject;
import edu.kit.exp.server.communication.ClientResponseMessage;
import edu.kit.exp.server.communication.ServerMessageSender;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import edu.kit.exp.server.jpa.entity.Treatment;

/**
 * This abstract class represents an economic institution.
 * 
 */
public abstract class Institution<T extends Environment> {

	protected T environment;
	protected boolean finished = false;
	protected List<Membership> memberships;
	protected SubjectGroup subjectGroup;
	private ServerMessageSender messageSender;
	private String gameId;
	private Treatment currentTreatment;
	private Period currentPeriod;

	
	/**
	 * Creates an Institution.
	 * 
	 * @param environment
	 *            The environment the economic institution interacts with,
	 *            within the economic system.
	 * @param memberships
	 *            A list of memberships.
	 * @param gameId
	 * @see edu.kit.exp.server.jpa.entity.Membership
	 */
	public Institution(T environment, List<Membership> memberships, ServerMessageSender messageSender, String gameId) {
		super();
		this.environment = environment;
		this.memberships = memberships;
		this.setMessageSender(messageSender);
		this.gameId = gameId;

		this.setSubjectGroup(memberships.get(0).getSubjectGroup());
	}

	
	/**
	 * This method is called to start an institution
	 * @throws Exception
	 */
	public abstract void startPeriod() throws Exception;

	/**
	 * This method processes client response messages.
	 * @param msg
	 * @throws Exception
	 */
	public abstract void processMessage(ClientResponseMessage msg) throws Exception;

	/**
	 * This method ends the institution.
	 * @throws Exception
	 */
	protected abstract void endPeriod() throws Exception;

	/**
	 * 
	 * Triggers, that a given screen is shown to the specified client. The
	 * Institution will wait until the subject has entered some requested input.
	 * Make sure the screen takes some input from the subject
	 * 
	 * @param subject
	 *            The subject that shod see the screen.
	 * @param screenId
	 *            Global screen ID
	 * @param parameter
	 *            The custom parameters of that screen.
	 * @throws Exception
	 */
	protected void showScreen(Subject subject, Class<? extends Screen> screenId, IScreenParamObject parameter) throws Exception {
		showScreen(subject.getIdClient(), screenId, parameter);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.exp.server.microeconomicsystem.Institution.showScreen(String, String, ArrayList<Object>)
	 */
	protected void showScreen(String clientId, Class<? extends Screen> screenId, IScreenParamObject parameter) throws Exception {
		getMessageSender().sendShowScreenMessage(clientId, screenId.getName(), parameter, gameId);
	}

	/**
	 * Shows the a screen at the client for the defined time
	 * 
	 * @param subject
	 *            The subject that shod see the screen.
	 * @param screenId
	 *            Global screen ID
	 * @param parameter
	 *            The custom parameters of that screen.
	 * @param showUpTime
	 *            Duration the defined screen will be visible. <b>IN MILLISECONDS</b>
	 * @throws RemoteException 
	 */
	protected void showScreenWithDeadLine(Subject subject, Class<? extends Screen> screenId, IScreenParamObject parameter, Long showUpTime) throws RemoteException {
		showScreenWithDeadLine(subject.getIdClient(), screenId, parameter, showUpTime);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.exp.server.microeconomicsystem.Institution.showScreenWithDeadLine(Subject subject, Class<? extends Screen> screenId, IScreenParamObject parameter, Long showUpTime)
	 */
	protected void showScreenWithDeadLine(String clientId, Class<? extends Screen> screenId, IScreenParamObject parameter, Long showUpTime) throws RemoteException {
		getMessageSender().sendShowScreenMessage(clientId, screenId.getName(), parameter, gameId, showUpTime);
	}

	/**
	 * Sends a ParamObject to a client without changing the current screen
	 * 
	 * @param subject
	 *            The subject that shod see the screen.
	 * @param parameter
	 *            The custom parameters of that screen.
	 * @throws RemoteException
	 */
	protected void updateParamObject(Subject subject, IScreenParamObject parameter) throws RemoteException {
		updateParamObject(subject.getIdClient(), parameter);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.exp.server.microeconomicsystem.Institution.updateParamParameter(Subject subject, IScreenParamObject parameter)
	 */
	protected void updateParamObject(String clientId, IScreenParamObject parameter) throws RemoteException {
		getMessageSender().sendParamObjectUpdate(clientId, parameter);
	}

	public boolean isFinished() {

		return finished;
	}

	public SubjectGroup getSubjectGroup() {
		return subjectGroup;
	}

	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	public T getEnvironment() {
		return environment;
	}

	public void setEnvironment(T environment) {
		this.environment = environment;
	}

	public List<Membership> getMemberships() {
		return memberships;
	}

	public void setMemberships(List<Membership> memberships) {
		this.memberships = memberships;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setCurrentTreatment(Treatment currentTreatment) {
		this.currentTreatment = currentTreatment;
	}

	public Period getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(Period currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public Treatment getCurrentTreatment() {
		return currentTreatment;
	}

	public void setMessageSender(ServerMessageSender messageSender) {
		this.messageSender = messageSender;
	}


	public ServerMessageSender getMessageSender() {
		return messageSender;
	}

}
