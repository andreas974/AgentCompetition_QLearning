package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Subject;

/**
 * Class provides CRUD functionalities for Subjects in DB.
 */
public class SubjectDAO implements ISubjectDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.ISubjectDAO#findAllSubjects()
	 */
	@Override
	public List<Subject> findAllSubjects() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Subject> results = null;

		try {

			TypedQuery<Subject> query = em.createNamedQuery("Subject.findAll", Subject.class);
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
	 * edu.kit.exp.server.jpa.dao.ISubjectDAO#findSubjectById(java.lang.Integer)
	 */
	@Override
	public Subject findSubjectById(Integer subjectId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Subject result = null;

		try {

			TypedQuery<Subject> query = em.createNamedQuery("Subject.findByIdSubject", Subject.class);
			query.setParameter("idSubject", subjectId);
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
	 * edu.kit.exp.server.jpa.dao.ISubjectDAO#createSubject(edu.kit.exp.server
	 * .jpa.entity.Subject)
	 */
	@Override
	public Subject createSubject(Subject subject) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Subject result;

		try {

			em.getTransaction().begin();
			em.persist(subject);
			em.getTransaction().commit();
			em.refresh(subject);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) util.getIdentifier(subject);

			result = findSubjectById(id);

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
	 * edu.kit.exp.server.jpa.dao.ISubjectDAO#updateSubject(edu.kit.exp.server
	 * .jpa.entity.Subject)
	 */
	@Override
	public Subject updateSubject(Subject subject) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Subject result = null;

		try {
			em.getTransaction().begin();

			// update
			result = em.merge(subject);
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
	 * edu.kit.exp.server.jpa.dao.ISubjectDAO#deleteSubject(edu.kit.exp.server
	 * .jpa.entity.Subject)
	 */
	@Override
	public void deleteSubject(Subject subject) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Subject s = em.find(Subject.class, subject.getIdSubject());
			Cohort c = s.getCohort();
			c.getSubjects().remove(subject);

			em.getTransaction().begin();
			em.merge(c);
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
