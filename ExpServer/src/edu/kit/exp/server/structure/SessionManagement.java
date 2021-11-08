package edu.kit.exp.server.structure;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.ISessionDAO;
import edu.kit.exp.server.jpa.dao.SessionDAO;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Session;

/**
 * This class provides all persistence functions of sessions.
 * 
 */
public class SessionManagement {

	private static SessionManagement instance;
	private ISessionDAO sessionDAO = new SessionDAO();

	/**
	 * Returns an instance of the SessionManagement.
	 * 
	 * @return
	 */
	public static SessionManagement getInstance() {

		if (instance == null) {
			instance = new SessionManagement();
		}

		return instance;
	}

	private SessionManagement() {

	}

	/**
	 * Finds a session in DB.
	 * 
	 * @param sessionId
	 * @return
	 * @throws StructureManagementException
	 */
	public Session findSession(Integer sessionId) throws StructureManagementException {

		Session session;

		try {
			session = sessionDAO.findSessionById(sessionId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Session could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return session;
	}

	/**
	 * Creates a new session for the given Experiment
	 * 
	 * @param experiment
	 * @return
	 * @throws StructureManagementException
	 */
	public Session createNewSession(Experiment experiment) throws StructureManagementException {

		Session session = new Session();
		Integer number = experiment.getSessions().size() + 1;
		session.setName("Session " + number.toString());
		session.setExperiment(experiment);
		DateTime date = new DateTime(2013, 01, 01, 00, 00);
		Timestamp plannedDate = new Timestamp(date.getMillis());
		session.setPlannedDate(plannedDate);

		Session result;

		try {
			result = sessionDAO.createSession(session);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Session could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Get all sessions from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Session> findAllSessions() throws StructureManagementException {

		List<Session> list;
		try {
			list = sessionDAO.findAllSessions();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Sessions could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a session from DB.
	 * 
	 * @param idSession
	 * @throws StructureManagementException
	 */
	public void deleteSession(Integer idSession) throws StructureManagementException {

		Session session;
		try {
			session = sessionDAO.findSessionById(idSession);
			sessionDAO.deleteSession(session);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Sessions could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	public Session updateSession(Session session) throws StructureManagementException {

		Session result;

		try {
			result = sessionDAO.updateSession(session);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Sessions could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
