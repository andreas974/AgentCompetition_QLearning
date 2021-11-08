package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import edu.kit.exp.server.jpa.entity.Trial;

/**
 * Class provides CRUD functionalities for Trials in DB.
 */
public class TrialDAO implements ITrialDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.ITrialDao#findAllTrials()
	 */
	@Override
	public List<Trial> findAllTrials() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Trial> results = null;

		try {

			TypedQuery<Trial> query = em.createNamedQuery("Trial.findAll", Trial.class);
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
	 * @see edu.kit.exp.server.jpa.dao.ITrialDao#findTrialById(java.lang.Long)
	 */
	@Override
	public Trial findTrialById(Long trialId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Trial result = null;

		try {

			TypedQuery<Trial> query = em.createNamedQuery("Trial.findByIdTrial", Trial.class);
			query.setParameter("idTrial", trialId);
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
	 * edu.kit.exp.server.jpa.dao.ITrialDao#findTrialsByName(java.lang.String)
	 */
	@Override
	public List<Trial> findTrialsByName(String trialName) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Trial> results = null;

		try {

			TypedQuery<Trial> query = em.createNamedQuery("Trial.findByName", Trial.class);
			query.setParameter("name", trialName);
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
	 * edu.kit.exp.server.jpa.dao.ITrialDao#createTrial(edu.kit.exp.server.jpa
	 * .entity.Trial)
	 */
	@Override
	public Trial createTrial(Trial trial) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Trial result;

		try {

			em.getTransaction().begin();
			em.persist(trial);
			em.getTransaction().commit();
			em.refresh(trial);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Long id = (Long) util.getIdentifier(trial);

			result = findTrialById(id);

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
	 * edu.kit.exp.server.jpa.dao.ITrialDao#updateTrial(edu.kit.exp.server.jpa
	 * .entity.Trial)
	 */
	@Override
	public Trial updateTrial(Trial trial) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Trial result = null;

		try {
			em.getTransaction().begin();
			// // find
			// Trial trialFromDB = em.find(Trial.class, trial.getIdTrial());
			// trialFromDB.setClientTime(trial.getClientTime());
			// trialFromDB.setEvent(trial.getEvent());
			// trialFromDB.setPeriod(trial.getPeriod());
			// trialFromDB.setScreenName(trial.getScreenName());
			// trialFromDB.setServerTime(trial.getServerTime());
			// trialFromDB.setSubject(trial.getSubject());
			// trialFromDB.setValue(trial.getValue());

			// update
			trial = em.merge(trial);
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
	 * edu.kit.exp.server.jpa.dao.ITrialDao#deleteTrial(edu.kit.exp.server.jpa
	 * .entity.Trial)
	 */
	@Override
	public void deleteTrial(Trial trial) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Trial t = em.find(Trial.class, trial.getIdTrial());
			SubjectGroup s = trial.getSubjectGroup();
			s.getTrials().remove(t);

			em.getTransaction().begin();
			em.merge(s);
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
