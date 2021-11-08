package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Protocol;
import edu.kit.exp.server.jpa.entity.Quiz;

/**
 * Class provides CRUD functionalities for Protocols in DB.
 */
public class ProtocolDAO implements IProtocolDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.IProtocolDAO#findAllProtocols()
	 */
	@Override
	public List<Protocol> findAllProtocols() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Protocol> results = null;

		try {

			TypedQuery<Protocol> query = em.createNamedQuery("Protocol.findAll", Protocol.class);
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
	 * @see edu.kit.exp.server.jpa.dao.IProtocolDAO#findProtocolById
	 * (java.lang.Integer)
	 */
	@Override
	public Protocol findProtocolById(Integer protocolId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Protocol result = null;

		try {

			TypedQuery<Protocol> query = em.createNamedQuery("Protocol.findByIdProtocol", Protocol.class);
			query.setParameter("idProtocol", protocolId);
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
	 * @see edu.kit.exp.server.jpa.dao.IProtocolDAO#updateProtocol(
	 * edu.kit.exp.server.jpa.entity.Protocol)
	 */
	@Override
	public Protocol updateProtocol(Protocol protocol) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Protocol result = null;

		try {
			em.getTransaction().begin();

			// // find
			// Protocol protocolFromDB = em.find(Protocol.class,
			// protocol.getIdprotocol());
			// protocolFromDB.setDone(protocol.getDone());
			// protocolFromDB.setSequenceNumber(protocol.getSequenceNumber());
			// protocolFromDB.setSession(protocol.getSession());
			// // protocolFromDB.set

			// update
			result = em.merge(protocol);

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
	 * @see edu.kit.exp.server.jpa.dao.IProtocolDAO#createProtocol(
	 * edu.kit.exp.server.jpa.entity.Protocol)
	 */
	@Override
	public Protocol createProtocol(Protocol protocol) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Protocol result;

		try {

			em.getTransaction().begin();
			em.persist(protocol);
			em.getTransaction().commit();
			em.refresh(protocol);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) util.getIdentifier(protocol);

			result = findProtocolById(id);

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
	 * @see edu.kit.exp.server.jpa.dao.IProtocolDAO#deleteProtocol(
	 * edu.kit.exp.server.jpa.entity.Protocol)
	 */
	@Override
	public void deleteProtocol(Protocol protocol) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Protocol p = em.find(Protocol.class, protocol.getIdprotocol());
			Quiz quiz = p.getQuiz();
			quiz.getProtocols().remove(p);

			em.getTransaction().begin();
			em.remove(p);
			em.merge(quiz);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}

	}

}
