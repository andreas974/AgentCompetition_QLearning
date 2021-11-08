package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Experiment;

/**
 * Class provides CRUD functionalities for experiments in DB.
 */
public class ExperimentDAO implements IExperimentDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.IExperimentDAO#findAllExperiments()
	 */
	@Override
	public List<Experiment> findAllExperiments() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Experiment> results = null;

		try {

			TypedQuery<Experiment> query = em.createNamedQuery("Experiment.findAll", Experiment.class);
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
	 * edu.kit.exp.server.jpa.dao.IExperimentDAO#findExperimentById(java.lang
	 * .Integer)
	 */
	@Override
	public Experiment findExperimentById(Integer experimentId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Experiment result = null;

		try {

			TypedQuery<Experiment> query = em.createNamedQuery("Experiment.findByIdExperiment", Experiment.class);
			query.setParameter("idExperiment", experimentId);
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
	 * edu.kit.exp.server.jpa.dao.IExperimentDAO#findExperimentsByName(java.
	 * lang.String)
	 */
	@Override
	public List<Experiment> findExperimentsByName(String experimentName) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Experiment> results = null;

		try {

			TypedQuery<Experiment> query = em.createNamedQuery("Experiment.findByName", Experiment.class);
			query.setParameter("name", experimentName);
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
	 * edu.kit.exp.server.jpa.dao.IExperimentDAO#createExperiment(edu.kit.exp
	 * .server.jpa.entity.Experiment)
	 */
	@Override
	public Experiment createExperiment(Experiment experiment) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Experiment result;

		try {

			em.getTransaction().begin();
			em.persist(experiment);
			em.getTransaction().commit();

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer experimentId = (Integer) util.getIdentifier(experiment);

			result = findExperimentById(experimentId);

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
	 * edu.kit.exp.server.jpa.dao.IExperimentDAO#updateExperiment(edu.kit.exp
	 * .server.jpa.entity.Experiment)
	 */
	@Override
	public Experiment updateExperiment(Experiment experiment) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Experiment result;

		try {
			em.getTransaction().begin();

			// update
			result = em.merge(experiment);
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
	 * edu.kit.exp.server.jpa.dao.IExperimentDAO#deleteExperiment(edu.kit.exp
	 * .server.jpa.entity.Experiment)
	 */
	@Override
	public void deleteExperiment(Experiment experiment) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Experiment exp = em.find(Experiment.class, experiment.getIdExperiment());
			em.getTransaction().begin();
			em.remove(exp);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}
	}

}
