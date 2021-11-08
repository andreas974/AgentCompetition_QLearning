package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Session;

/**
 * Class provides CRUD functionalities for Sessions in DB.
 */
public class SessionDAO implements ISessionDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.ISessionDAO#findAllSessions()
	 */
	@Override
	public List<Session> findAllSessions() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Session> results = null;

		try {

			TypedQuery<Session> query = em.createNamedQuery("Session.findAll", Session.class);
			results = query.getResultList();

		} catch (Exception e) {

			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;

		} finally {
			em.close();
		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.jpa.dao.ISessionDAO#findSessionById(java.lang.Integer)
	 */
	@Override
	public Session findSessionById(Integer sessionId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Session result = null;

		try {

			TypedQuery<Session> query = em.createNamedQuery("Session.findByIdSession", Session.class);
			query.setParameter("idSession", sessionId);
			result = query.getSingleResult();

		} catch (Exception e) {

			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;

		} finally {
			em.close();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.jpa.dao.ISessionDAO#findSessionsByName(java.lang.String
	 * )
	 */
	@Override
	public List<Session> findSessionsByName(String sessionName) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Session> results = null;

		try {

			TypedQuery<Session> query = em.createNamedQuery("Session.findByName", Session.class);
			query.setParameter("name", sessionName);
			results = query.getResultList();

		} catch (Exception e) {

			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;

		} finally {
			em.close();
		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.jpa.dao.ISessionDAO#createSession(edu.kit.exp.server
	 * .jpa.entity.Session)
	 */
	@Override
	public Session createSession(Session session) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Session result;

		try {

			em.getTransaction().begin();
			em.persist(session);
			em.getTransaction().commit();
			em.refresh(session);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) util.getIdentifier(session);

			result = findSessionById(id);

		} catch (Exception e) {

			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;

		} finally {
			em.close();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.jpa.dao.ISessionDAO#updateSession(edu.kit.exp.server
	 * .jpa.entity.Session)
	 */
	@Override
	public Session updateSession(Session session) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Session result;

		try {
			em.getTransaction().begin();
			// find
			// Session sessionFromDB = em.find(Session.class,
			// session.getIdSession());
			// sessionFromDB.setName(session.getName());
			// sessionFromDB.setDescription(session.getDescription());
			// sessionFromDB.setExperiment(session.getExperiment());
			// sessionFromDB.setCohortes(session.getCohorts());
			// //
			// sessionFromDB.setTreatmentBlocks(session.getTreatmentBlocks());
			// session.setSequenceElements(session.getSequenceElements());
			// sessionFromDB.setDone(session.getDone());
			// sessionFromDB.setPlannedDate(session.getPlannedDate());

			// update
			result = em.merge(session);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.jpa.dao.ISessionDAO#deleteSession(edu.kit.exp.server
	 * .jpa.entity.Session)
	 */
	@Override
	public void deleteSession(Session session) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Session s = em.find(Session.class, session.getIdSession());
			Experiment e = session.getExperiment();
			e.getSessions().remove(s);

			em.getTransaction().begin();
			em.merge(e);
			em.remove(s);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}
	}
}
