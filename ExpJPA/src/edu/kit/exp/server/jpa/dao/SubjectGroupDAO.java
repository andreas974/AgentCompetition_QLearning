package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * Class provides CRUD functionalities for SubjectGroups in DB.
 */
public class SubjectGroupDAO implements ISubjectGroupDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.ISubjectGroupDAO#findAllSubjectGroups()
	 */
	@Override
	public List<SubjectGroup> findAllSubjectGroups() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<SubjectGroup> results = null;

		try {

			TypedQuery<SubjectGroup> query = em.createNamedQuery("SubjectGroup.findAll", SubjectGroup.class);
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
	 * edu.kit.exp.server.jpa.dao.ISubjectGroupDAO#findSubjectGroupsById(java
	 * .lang.Integer)
	 */
	@Override
	public SubjectGroup findSubjectGroupById(Long subjectGroupId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		SubjectGroup result = null;

		try {

			TypedQuery<SubjectGroup> query = em.createNamedQuery("SubjectGroup.findByIdSubjectGroup", SubjectGroup.class);
			query.setParameter("idSubjectGroup", subjectGroupId);
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
	 * edu.kit.exp.server.jpa.dao.ISubjectGroupDAO#createSubjectGroup(edu.kit
	 * .exp.server.jpa.entity.SubjectGroup)
	 */
	@Override
	public SubjectGroup createSubjectGroup(SubjectGroup subjectGroup) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		SubjectGroup result;

		try {

			em.getTransaction().begin();
			em.persist(subjectGroup);
			em.getTransaction().commit();
			em.refresh(subjectGroup);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Long id = (Long) util.getIdentifier(subjectGroup);

			result = findSubjectGroupById(id);

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
	 * edu.kit.exp.server.jpa.dao.ISubjectGroupDAO#updateSubjectGroup(edu.kit
	 * .exp.server.jpa.entity.SubjectGroup)
	 */
	@Override
	public SubjectGroup updateSubjectGroup(SubjectGroup subjectGroup) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		SubjectGroup result;

		try {
			em.getTransaction().begin();

			result = em.merge(subjectGroup);
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
	 * edu.kit.exp.server.jpa.dao.ISubjectGroupDAO#deleteSubjectGroup(edu.kit
	 * .exp.server.jpa.entity.SubjectGroup)
	 */
	@Override
	public void deleteSubjectGroup(SubjectGroup subjectGroup) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			SubjectGroup exp = em.find(SubjectGroup.class, subjectGroup.getIdSubjectGroup());
			Period p = subjectGroup.getPeriod();
			p.getSubjectGroups().remove(subjectGroup);

			em.getTransaction().begin();
			em.merge(p);
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
