package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;

///**
// * Class provides CRUD functionalities for TreatmentBlocks in DB.
// */
public class TreatmentBlockDAO implements ITreatmentBlockDAO {

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// * edu.kit.exp.server.jpa.dao.ITreatmentBlockDAO#findAllTreatmentBlocks()
	// */
	@Override
	public List<TreatmentBlock> findAllTreatmentBlocks() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<TreatmentBlock> results = null;

		try {

			TypedQuery<TreatmentBlock> query = em.createNamedQuery("TreatmentBlock.findAll", TreatmentBlock.class);
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
	 * // * (non-Javadoc) // * // * @see // *
	 * edu.kit.exp.server.jpa.dao.ITreatmentBlockDAO#findTreatmentBlockById( //
	 * * java.lang.Integer) //
	 */
	@Override
	public TreatmentBlock findTreatmentBlockById(Integer treatmentBlockId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		TreatmentBlock result = null;

		try {

			TypedQuery<TreatmentBlock> query = em.createNamedQuery("TreatmentBlock.findByIdTreatment", TreatmentBlock.class);
			query.setParameter("idTreatmentBlock", treatmentBlockId);
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
	 * edu.kit.exp.server.jpa.dao.ITreatmentBlockDAO#createTreatmentBlock(edu
	 * .kit.exp.server.jpa.entity.TreatmentBlock)
	 */
	@Override
	public TreatmentBlock createTreatmentBlock(TreatmentBlock treatmentBlock) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		TreatmentBlock result;

		try {

			em.getTransaction().begin();
			em.persist(treatmentBlock);
			em.getTransaction().commit();
			em.refresh(treatmentBlock);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) util.getIdentifier(treatmentBlock);

			result = findTreatmentBlockById(id);

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
	 * edu.kit.exp.server.jpa.dao.ITreatmentBlockDAO#updateTreatmentBlock(edu
	 * .kit.exp.server.jpa.entity.TreatmentBlock)
	 */
	@Override
	public TreatmentBlock updateTreatmentBlock(TreatmentBlock treatmentBlock) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		TreatmentBlock result;

		try {
			em.getTransaction().begin();

			// find
			// TreatmentBlock treatmentBlockFromDB =
			// em.find(TreatmentBlock.class,
			// treatmentBlock.getIdsequenceElement());
			// treatmentBlockFromDB.setName(treatmentBlock.getName());
			// treatmentBlockFromDB.setDescription(treatmentBlock.getDescription());
			// treatmentBlockFromDB.setTreatment(treatmentBlock.getTreatment());
			// treatmentBlockFromDB.setPeriods(treatmentBlock.getPeriods());
			// treatmentBlockFromDB.setSession(treatmentBlock.getSession());

			// update
			result = em.merge(treatmentBlock);
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
	 * edu.kit.exp.server.jpa.dao.ITreatmentBlockDAO#deleteTreatmentBlock(edu
	 * .kit.exp.server.jpa.entity.TreatmentBlock)
	 */
	@Override
	public void deleteTreatmentBlock(TreatmentBlock treatmentBlock) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			TreatmentBlock exp = em.find(TreatmentBlock.class, treatmentBlock.getIdsequenceElement());
			Session s = treatmentBlock.getSession();
			s.getSequenceElements().remove(treatmentBlock);

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
