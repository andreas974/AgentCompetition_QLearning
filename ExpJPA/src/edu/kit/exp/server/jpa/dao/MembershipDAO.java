package edu.kit.exp.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.PersistenceUtil;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Subject;

/**
 * Class provides CRUD functionalities for Memberships in DB.
 */
public class MembershipDAO implements IMembershipDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.kit.exp.server.jpa.dao.IMembershipDAO#findAllMemberships()
	 */
	@Override
	public List<Membership> findAllMemberships() throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		List<Membership> results = null;

		try {

			TypedQuery<Membership> query = em.createNamedQuery("Membership.findAll", Membership.class);
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
	 * edu.kit.exp.server.jpa.dao.IMembershipDAO#findMembershipById(java.lang
	 * .Integer)
	 */
	@Override
	public Membership findMembershipById(Long membershipId) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Membership result = null;

		try {

			TypedQuery<Membership> query = em.createNamedQuery("Membership.findByIdMembership", Membership.class);
			query.setParameter("idMembership", membershipId);
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
	 * edu.kit.exp.server.jpa.dao.IMembershipDAO#updateMembership(edu.kit.exp
	 * .server.jpa.entity.Membership)
	 */
	@Override
	public Membership updateMembership(Membership membership) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Membership result = null;

		try {
			em.getTransaction().begin();

			// // find
			// Membership membershipFromDB = em.find(Membership.class,
			// membership.getIdmembership());
			// membershipFromDB.setDone(membership.getDone());
			// membershipFromDB.setSequenceNumber(membership.getSequenceNumber());
			// membershipFromDB.setSession(membership.getSession());
			// // membershipFromDB.set

			// update
			result = em.merge(membership);

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
	 * edu.kit.exp.server.jpa.dao.IMembershipDAO#createMembership(edu.kit.exp
	 * .server.jpa.entity.Membership)
	 */
	@Override
	public Membership createMembership(Membership membership) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();
		Membership result;

		try {

			em.getTransaction().begin();
			em.persist(membership);
			em.getTransaction().commit();
			em.refresh(membership);

			PersistenceUnitUtil util = PersistenceUtil.getEntitymanagerFactory().getPersistenceUnitUtil();
			Long id = (Long) util.getIdentifier(membership);

			result = findMembershipById(id);

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
	 * @see edu.kit.exp.server.jpa.dao.IMembershipDAO#deleteMembership(
	 * edu.kit.exp.server.jpa.entity.Membership)
	 */
	@Override
	public void deleteMembership(Membership membership) throws DataManagementException {

		EntityManager em = PersistenceUtil.createEntityManager();

		try {

			Membership m = em.find(Membership.class, membership.getIdMembership());
			Subject subject = m.getSubject();
			subject.getMemberships().remove(m);

			em.getTransaction().begin();
			em.remove(m);
			em.merge(subject);
			em.getTransaction().commit();

		} catch (Exception e) {
			DataManagementException ex = new DataManagementException(e.getMessage());
			throw ex;
		} finally {
			em.close();
		}

	}

}
