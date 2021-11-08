package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Membership;

/**
 * Provides CRUD functionalities for Memberships in DB.
 */
public interface IMembershipDAO {

	/**
	 * Finds all memberships in DB.
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Membership> findAllMemberships() throws DataManagementException;

	/**
	 * Finds a membership by id.
	 * 
	 * @param membershipId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Membership findMembershipById(Long membershipId) throws DataManagementException;

	/**
	 * Updates a membership.
	 * 
	 * @param membership
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Membership updateMembership(Membership membership) throws DataManagementException;

	/**
	 * Creates a membership in db.
	 * 
	 * @param membership
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Membership createMembership(Membership membership) throws DataManagementException;

	/**
	 * Removes a membership from db.
	 * 
	 * @param membership
	 * @throws DataManagementException
	 */
	public abstract void deleteMembership(Membership membership) throws DataManagementException;

}