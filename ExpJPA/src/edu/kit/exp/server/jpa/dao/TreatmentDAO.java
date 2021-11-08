package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Treatment;

/**
 * Class provides CRUD functionalities for treatments in DB.
 */
public class TreatmentDAO implements ITreatmentDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.ITreatmentDAO#findAllTreatments()
	 */
	@Override
	public List<Treatment> findAllTreatments() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Treatment> results = null;

		try {

			TypedQuery<Treatment> query = em.createNamedQuery("Treatment.findAll", Treatment.class);
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
	 * edu.kit.exp.server.jpa.dao.ITreatmentDAO#findTreatmentsById(java.lang
	 * .Integer)
	 */
	@Override
	public Treatment findTreatmentById(Integer treatmentId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Treatment result = null;

		try {

			TypedQuery<Treatment> query = em.createNamedQuery("Treatment.findByIdTreatment", Treatment.class);
			query.setParameter("idTreatment", treatmentId);
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
	 * edu.kit.exp.server.jpa.dao.ITreatmentDAO#findTreatmentsByName(java.lang
	 * .String)
	 */
	@Override
	public List<Treatment> findTreatmentsByName(String treatmentName) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Treatment> results = null;

		try {

			TypedQuery<Treatment> query = em.createNamedQuery("Treatment.findByName", Treatment.class);
			query.setParameter("name", treatmentName);
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
	 * edu.kit.exp.server.jpa.dao.ITreatmentDAO#createTreatment(edu.kit.exp.
	 * server.jpa.entity.Treatment)
	 */
	@Override
	public Treatment createTreatment(Treatment treatment) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Treatment result;

		try {

			em.getTransaction().begin();
			em.persist(treatment);
			em.getTransaction().commit();
			em.refresh(treatment);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) util.getIdentifier(treatment);

			result = findTreatmentById(id);

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
	 * edu.kit.exp.server.jpa.dao.ITreatmentDAO#updateTreatment(edu.kit.exp.
	 * server.jpa.entity.Treatment)
	 */
	@Override
	public Treatment updateTreatment(Treatment treatment) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Treatment result;

		try {
			em.getTransaction().begin();
			// // find
			// Treatment treatmentFromDB = em.find(Treatment.class,
			// treatment.getIdTreatment());
			// treatmentFromDB.setName(treatment.getName());
			// treatmentFromDB.setDescription(treatment.getDescription());
			// treatmentFromDB.setTreatmentBlocks(treatment.getTreatmentBlocks());
			// treatmentFromDB.setEnvironmentFactoryKey(treatment.getEnvironmentFactoryKey());
			// treatmentFromDB.setInstitutionFactoryKey(treatment.getInstitutionFactoryKey());

			// update
			result = em.merge(treatment);
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
	 * edu.kit.exp.server.jpa.dao.ITreatmentDAO#deleteTreatment(edu.kit.exp.
	 * server.jpa.entity.Treatment)
	 */
	@Override
	public void deleteTreatment(Treatment treatment) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Treatment t = em.find(Treatment.class, treatment.getIdTreatment());

			em.getTransaction().begin();
			em.remove(t);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}
	}

}
