package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Session;

/**
 * Provides CRUD functionalities for Sessions in DB.
 */
public interface ISessionDAO {

	/**
	 * Returns all Sessions from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Session> findAllSessions() throws DataManagementException;

	/**
	 * Finds a Session by SessionId.
	 * 
	 * @param sessionId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Session findSessionById(Integer sessionId) throws DataManagementException;

	/**
	 * Finds a Session by name.
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Session> findSessionsByName(String sessionName) throws DataManagementException;

	/**
	 * Creates a Session in DB.
	 * 
	 * @param session
	 * @throws DataManagementException
	 */
	public abstract Session createSession(Session session) throws DataManagementException;

	/**
	 * Creates a Session in DB.
	 * 
	 * @param session
	 * @throws DataManagementException
	 */
	public abstract Session updateSession(Session session) throws DataManagementException;

	/**
	 * Removes a Session from DB.
	 * 
	 * @param session
	 * @throws DataManagementException
	 */
	public abstract void deleteSession(Session session) throws DataManagementException;

}