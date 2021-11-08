package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.ITreatmentDAO;
import edu.kit.exp.server.jpa.dao.TreatmentDAO;
import edu.kit.exp.server.jpa.entity.Treatment;

/**
 * This class provides all functions of treatments.
 * 
 */
public class TreatmentManagement {

	private static TreatmentManagement instance;
	private ITreatmentDAO treatmentDAO = new TreatmentDAO();

	/**
	 * Returns an instance of the TreatmentManagement.
	 * 
	 * @return
	 */
	public static TreatmentManagement getInstance() {

		if (instance == null) {
			instance = new TreatmentManagement();
		}

		return instance;
	}

	private TreatmentManagement() {

	}

	/**
	 * Finds a treatment in DB.
	 * 
	 * @param treatmentId
	 * @return
	 * @throws StructureManagementException
	 */
	public Treatment findTreatment(Integer treatmentId) throws StructureManagementException {

		Treatment treatment;

		try {
			treatment = treatmentDAO.findTreatmentById(treatmentId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Treatment could not be found. Cause: "+e.getMessage());
			throw ex;
		}

		return treatment;
	}

	/**
	 * Creates a new treatment.
	 * @param treatment 
	 * @param experiment
	 * @return 
	 * @throws StructureManagementException
	 */
	public Treatment createNewTreatment(Treatment treatment) throws StructureManagementException {

//		Treatment t = new Treatment();
//		long number = new Date().getTime();
//		
//		t.setName("New Treatment");
//		t.setInstitutionFactoryKey("CHANGE THIS ENTRY! "+number);
//		t.setEnvironmentFactoryKey("CHANGE THIS ENTRY! "+number);
		
		Treatment result;

		try {
			result = treatmentDAO.createTreatment(treatment);
		} catch (DataManagementException e) {
			
			StructureManagementException ex = new StructureManagementException("Treatment could not be created. Cause: "+e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Get all treatments from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Treatment> findAllTreatments() throws StructureManagementException {

		List<Treatment> list;
		try {
			list = treatmentDAO.findAllTreatments();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Treatments could not be found. Cause: "+e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a treatment from DB.
	 * @param idTreatment
	 * @throws StructureManagementException
	 */
	public void deleteTreatment(Treatment treatment) throws StructureManagementException {

		
		try {
//			treatment = treatmentDAO.findTreatmentById(idTreatment);
			treatmentDAO.deleteTreatment(treatment);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Treatment could not be found. Cause: "+e.getMessage());
			throw ex;
		}
	}

	/**
	 * Updates a given Treatment. 
	 * @param treatment
	 * @return
	 * @throws StructureManagementException
	 */
	public Treatment updateTreatment(Treatment treatment) throws StructureManagementException {
		
		Treatment result;
		try {
			result = treatmentDAO.updateTreatment(treatment);
			
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Treatments could not be found. Cause: "+e.getMessage());
			throw ex;
		}
		
		return result;
		
	}
}
