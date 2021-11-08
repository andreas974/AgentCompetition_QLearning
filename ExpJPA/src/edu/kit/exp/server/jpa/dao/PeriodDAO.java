package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;

/**
 * Class provides CRUD functionalities for Periods in DB.
 */
public class PeriodDAO implements IPeriodDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.IPeriodDAO#findAllPeriods()
	 */
	@Override
	public List<Period> findAllPeriods() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Period> results = null;

		try {

			TypedQuery<Period> query = em.createNamedQuery("Period.findAll", Period.class);
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
	 * edu.kit.exp.server.jpa.dao.IPeriodDAO#findPeriodsById(java.lang.Integer)
	 */
	@Override
	public Period findPeriodById(Integer periodId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Period result = null;

		try {

			TypedQuery<Period> query = em.createNamedQuery("Period.findByIdPeriod", Period.class);
			query.setParameter("idPeriod", periodId);
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
	 * edu.kit.exp.server.jpa.dao.IPeriodDAO#createPeriod(edu.kit.exp.server
	 * .jpa.entity.Period)
	 */
	@Override
	public Period createPeriod(Period period) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Period result;

		try {

			em.getTransaction().begin();
			em.persist(period);
			em.getTransaction().commit();
			em.refresh(period);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) util.getIdentifier(period);

			result = findPeriodById(id);

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
	 * edu.kit.exp.server.jpa.dao.IPeriodDAO#updatePeriod(edu.kit.exp.server
	 * .jpa.entity.Period)
	 */
	@Override
	public Period updatePeriod(Period period) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Period result;

		try {
			em.getTransaction().begin();
			// find
			// Period periodFromDB = em.find(Period.class,
			// period.getIdPeriod());
			// periodFromDB.setTrials(period.getTrials());
			// periodFromDB.setTreatmentBlock(period.getTreatmentBlock());
			// periodFromDB.setSubjectGroups(period.getSubjectGroups());
			// periodFromDB.setDone(period.getDone());
			// periodFromDB.setSequenceNumber(period.getSequenceNumber());

			// update
			result = em.merge(period);
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
	 * edu.kit.exp.server.jpa.dao.IPeriodDAO#deletePeriod(edu.kit.exp.server
	 * .jpa.entity.Period)
	 */
	@Override
	public void deletePeriod(Period period) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Period p = em.find(Period.class, period.getIdPeriod());
			TreatmentBlock tb = period.getTreatmentBlock();
			tb.getPeriods().remove(period);

			em.getTransaction().begin();
			em.remove(p);
			em.merge(tb);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}
	}

}
