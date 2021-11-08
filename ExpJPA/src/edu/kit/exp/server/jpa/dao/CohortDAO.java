package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Session;

/**
 * Class provides CRUD functionalities for Cohorts in DB.
 */
public class CohortDAO implements ICohortDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.ICohortDAO#findAllCohorts()
	 */
	@Override
	public List<Cohort> findAllCohorts() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Cohort> results = null;

		try {

			TypedQuery<Cohort> query = em.createNamedQuery("Cohort.findAll", Cohort.class);
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
	 * edu.kit.exp.server.jpa.dao.ICohortDAO#findCohortById(java.lang.Integer)
	 */
	@Override
	public Cohort findCohortById(Integer cohortId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Cohort result = null;

		try {

			TypedQuery<Cohort> query = em.createNamedQuery("Cohort.findByIdCohort", Cohort.class);
			query.setParameter("idCohort", cohortId);
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
	 * edu.kit.exp.server.jpa.dao.ICohortDAO#createCohort(edu.kit.exp.server
	 * .jpa.entity.Cohort)
	 */
	@Override
	public Cohort createCohort(Cohort cohort) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Cohort result;

		try {

			em.getTransaction().begin();
			em.persist(cohort);
			em.getTransaction().commit();
			em.refresh(cohort);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) util.getIdentifier(cohort);

			result = findCohortById(id);

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
	 * edu.kit.exp.server.jpa.dao.ICohortDAO#updateCohort(edu.kit.exp.server
	 * .jpa.entity.Cohort)
	 */
	@Override
	public Cohort updateCohort(Cohort cohort) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Cohort result;

		try {
			em.getTransaction().begin();

			// find
			Cohort cohortFromDB = em.find(Cohort.class, cohort.getIdCohort());
			cohortFromDB.setSubjects(cohort.getSubjects());
			cohortFromDB.setSession(cohort.getSession());
			cohortFromDB.setSize(cohort.getSize());

			// update
			result = em.merge(cohort);
			em.getTransaction().commit();

			result = em.find(Cohort.class, cohort.getIdCohort());

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
	 * edu.kit.exp.server.jpa.dao.ICohortDAO#deleteCohort(edu.kit.exp.server
	 * .jpa.entity.Cohort)
	 */
	@Override
	public void deleteCohort(Cohort cohort) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Cohort c = em.find(Cohort.class, cohort.getIdCohort());
			Session s = cohort.getSession();
			s.getCohorts().remove(cohort);

			em.getTransaction().begin();
			em.remove(c);
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
