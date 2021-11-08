package edu.kit.exp.server.jpa.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.SequenceElement;
import edu.kit.exp.server.jpa.entity.Session;

/**
 * Class provides CRUD functionalities for SequenceElements in DB.
 */
public class SequenceElementDAO implements ISequenceElementDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.jpa.dao.ISequenceElementDAO#findAllSequenceElements()
	 */
	@Override
	public List<SequenceElement> findAllSequenceElements() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<SequenceElement> results = null;

		try {

			TypedQuery<SequenceElement> query = em.createNamedQuery("SequenceElement.findAll", SequenceElement.class);
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
	 * edu.kit.exp.server.jpa.dao.ISequenceElementDAO#findSequenceElementById
	 * (java.lang.Integer)
	 */
	@Override
	public SequenceElement findSequenceElementById(Integer sequenceElementId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		SequenceElement result = null;

		try {

			TypedQuery<SequenceElement> query = em.createNamedQuery("SequenceElement.findByIdSequenceElement", SequenceElement.class);
			query.setParameter("idSequenceElement", sequenceElementId);
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
	 * edu.kit.exp.server.jpa.dao.ISequenceElementDAO#updateSequenceElement(
	 * edu.kit.exp.server.jpa.entity.SequenceElement)
	 */
	@Override
	public List<SequenceElement> updateSequenceElement(List<SequenceElement> sequenceElements) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<SequenceElement> result = new ArrayList<SequenceElement>();
		SequenceElement s;

		for (SequenceElement sequenceElement : sequenceElements) {

			try {
				em.getTransaction().begin();

				// // find
				// SequenceElement sequenceElementFromDB =
				// em.find(SequenceElement.class,
				// sequenceElement.getIdsequenceElement());
				// sequenceElementFromDB.setDone(sequenceElement.getDone());
				// sequenceElementFromDB.setSequenceNumber(sequenceElement.getSequenceNumber());
				// sequenceElementFromDB.setSession(sequenceElement.getSession());
				// // sequenceElementFromDB.set

				// update
				s = em.merge(sequenceElement);
				result.add(s);

				em.getTransaction().commit();

			} catch (Exception e) {
				DataManagementException ex = new DataManagementException(e.getMessage());
				throw ex;
			} finally {
				em.close();
			}

		}
		return result;

	}

	@Override
	public SequenceElement updateSequenceElement(SequenceElement sequenceElement) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		SequenceElement s;

		try {
			em.getTransaction().begin();

			// // find
			// SequenceElement sequenceElementFromDB =
			// em.find(SequenceElement.class,
			// sequenceElement.getIdsequenceElement());
			// sequenceElementFromDB.setDone(sequenceElement.getDone());
			// sequenceElementFromDB.setSequenceNumber(sequenceElement.getSequenceNumber());
			// sequenceElementFromDB.setSession(sequenceElement.getSession());
			// // sequenceElementFromDB.set

			// update
			s = em.merge(sequenceElement);

			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();

		}
		return s;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.exp.server.jpa.dao.ISequenceElementDAO#createSequenceElement(
	 * edu.kit.exp.server.jpa.entity.SequenceElement)
	 */
	@Override
	public SequenceElement createSequenceElement(SequenceElement sequenceElement) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		SequenceElement result;

		try {

			em.getTransaction().begin();
			em.persist(sequenceElement);
			em.getTransaction().commit();
			em.refresh(sequenceElement);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) util.getIdentifier(sequenceElement);

			result = findSequenceElementById(id);

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
	 * edu.kit.exp.server.jpa.dao.ISequenceElementDAO#deleteSequenceElement(
	 * edu.kit.exp.server.jpa.entity.SequenceElement)
	 */
	@Override
	public void deleteSequenceElement(SequenceElement sequenceElement) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			SequenceElement exp = em.find(SequenceElement.class, sequenceElement.getIdsequenceElement());
			Session s = sequenceElement.getSession();
			s.getSequenceElements().remove(sequenceElement);

			em.getTransaction().begin();
			em.remove(exp);
			em.merge(s);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}

	}

}
