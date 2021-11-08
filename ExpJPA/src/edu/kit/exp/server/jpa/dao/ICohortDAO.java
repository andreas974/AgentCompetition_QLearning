package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Cohort;

/**
 * Provides CRUD functionalities for Cohorts in DB.
 * 
 */
public interface ICohortDAO {

	/**
	 * Returns all Cohorts from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Cohort> findAllCohorts() throws DataManagementException;

	/**
	 * Finds a Cohort by CohortId.
	 * 
	 * @param cohortId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Cohort findCohortById(Integer cohortId) throws DataManagementException;

	/**
	 * Creates a Cohort in DB.
	 * 
	 * @param cohort
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Cohort createCohort(Cohort cohort) throws DataManagementException;

	/**
	 * Creates a Cohort in DB.
	 * 
	 * @param cohort
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Cohort updateCohort(Cohort cohort) throws DataManagementException;

	/**
	 * Removes a Cohort from DB.
	 * 
	 * @param cohort
	 * @throws DataManagementException
	 */
	public abstract void deleteCohort(Cohort cohort) throws DataManagementException;

}