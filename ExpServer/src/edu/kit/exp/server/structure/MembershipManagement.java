package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.IMembershipDAO;
import edu.kit.exp.server.jpa.dao.MembershipDAO;
import edu.kit.exp.server.jpa.entity.Membership;

/**
 * This class provides all persistence functions of treatment blocks.
 * 
 */
public class MembershipManagement {

	private static MembershipManagement instance;
	private IMembershipDAO membershipDAO = new MembershipDAO();

	/**
	 * Returns an instance of the MembershipManagement.
	 * 
	 * @return
	 */
	public static MembershipManagement getInstance() {

		if (instance == null) {
			instance = new MembershipManagement();
		}

		return instance;
	}

	private MembershipManagement() {

	}

	/**
	 * Finds a membership in DB.
	 * 
	 * @param membershipId
	 * @return
	 * @throws StructureManagementException
	 */
	public Membership findMembership(Long membershipId) throws StructureManagementException {

		Membership membership;

		try {
			membership = membershipDAO.findMembershipById(membershipId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Membership could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return membership;
	}

	/**
	 * Creates new memberships for the given TreatmentBlock
	 * 
	 * @param treatmentBlock
	 * @return
	 * @throws StructureManagementException
	 */
	public Membership createNewMemberships(Membership membership) throws StructureManagementException {

		Membership result;

		try {
			result = membershipDAO.createMembership(membership);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Membership could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Get all memberships from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Membership> findAllMemberships() throws StructureManagementException {

		List<Membership> list;
		try {
			list = membershipDAO.findAllMemberships();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Memberships could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a membership from DB.
	 * 
	 * @param idMembership
	 * @throws StructureManagementException
	 */
	public void deleteMembership(Long idMembership) throws StructureManagementException {

		Membership membership;
		try {
			membership = membershipDAO.findMembershipById(idMembership);
			membershipDAO.deleteMembership(membership);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Membership could not be deleted. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * Updates a given Membership.
	 * 
	 * @param membership
	 * @return
	 * @throws StructureManagementException
	 */
	public Membership updateMembership(Membership membership) throws StructureManagementException {

		Membership result;
		try {
			result = membershipDAO.updateMembership(membership);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Membership could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
