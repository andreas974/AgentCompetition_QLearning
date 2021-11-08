package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Treatment;

/**
 * Provides CRUD functionalities for Treatments in DB.
 */
public interface ITreatmentDAO {

	/**
	 * Returns all treatments from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Treatment> findAllTreatments() throws DataManagementException;

	/**
	 * Finds a treatment by treatmentId.
	 * 
	 * @param treatmentId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Treatment findTreatmentById(Integer treatmentId) throws DataManagementException;

	/**
	 * Finds a treatment by name.
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Treatment> findTreatmentsByName(String treatmentName) throws DataManagementException;

	/**
	 * Creates a treatment in DB.
	 * 
	 * @param treatment
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Treatment createTreatment(Treatment treatment) throws DataManagementException;

	/**
	 * Creates a treatment in DB.
	 * 
	 * @param treatment
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Treatment updateTreatment(Treatment treatment) throws DataManagementException;

	/**
	 * Removes a treatment from DB.
	 * 
	 * @param treatment
	 * @throws DataManagementException
	 */
	public abstract void deleteTreatment(Treatment treatment) throws DataManagementException;

}