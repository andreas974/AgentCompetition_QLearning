package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.CohortDAO;
import edu.kit.exp.server.jpa.dao.ICohortDAO;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Session;

/**
 * This class provides all persistence functions of cohorts.
 * 
 */
public class CohortManagement {

	private static CohortManagement instance;
	private ICohortDAO cohortDAO = new CohortDAO();

	/**
	 * Returns an instance of the CohortManagement.
	 * 
	 * @return
	 */
	public static CohortManagement getInstance() {

		if (instance == null) {
			instance = new CohortManagement();
		}

		return instance;
	}

	private CohortManagement() {

	}

	/**
	 * Finds a Cohort in DB.
	 * 
	 * @param cohortId
	 * @return
	 * @throws StructureManagementException
	 */
	public Cohort findCohort(Integer cohortId) throws StructureManagementException {

		Cohort treatmentBlock;

		try {
			treatmentBlock = cohortDAO.findCohortById(cohortId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Cohort could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return treatmentBlock;
	}

	/**
	 * Creates a new Cohort for the given Session.
	 * 
	 * @param session
	 * @param cohortSize 
	 * @return
	 * @throws StructureManagementException
	 */
	public Cohort createNewCohort(Session session, Integer cohortSize) throws StructureManagementException {

		Cohort cohort = new Cohort();
		cohort.setSession(session);
		cohort.setSize(cohortSize);
						
		Cohort result;

		try {
			result = cohortDAO.createCohort(cohort);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Cohort could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Get all Cohorts from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Cohort> findAllCohorts() throws StructureManagementException {

		List<Cohort> list;
		try {
			list = cohortDAO.findAllCohorts();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Cohorts could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a Cohort from DB.
	 * 
	 * @param idCohort
	 * @throws StructureManagementException
	 */
	public void deleteCohort(Cohort sequenceElement) throws StructureManagementException {

		try {
			cohortDAO.deleteCohort(sequenceElement);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Cohort could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * Updates a given Cohort.
	 * 
	 * @param treatmentBlock
	 * @return
	 * @throws StructureManagementException
	 */
	public Cohort updateCohort(Cohort cohort) throws StructureManagementException {

		Cohort result;
		try {
			result = cohortDAO.updateCohort(cohort);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Cohort could not be removed. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

}
