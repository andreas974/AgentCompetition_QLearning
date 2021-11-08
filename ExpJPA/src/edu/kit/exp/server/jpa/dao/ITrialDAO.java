package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Trial;

/**
 * Provides CRUD functionalities for Trials in DB.
 */
public interface ITrialDAO {

	/**
	 * Returns all Trials from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Trial> findAllTrials() throws DataManagementException;

	/**
	 * Finds a Trial by trialId.
	 * 
	 * @param trialId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Trial findTrialById(Long trialId) throws DataManagementException;

	/**
	 * Finds a Trial by name.
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Trial> findTrialsByName(String trialName) throws DataManagementException;

	/**
	 * Creates a Trial in DB.
	 * 
	 * @param trial
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Trial createTrial(Trial trial) throws DataManagementException;

	/**
	 * Creates a Trial in DB.
	 * 
	 * @param trial
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Trial updateTrial(Trial trial) throws DataManagementException;

	/**
	 * Removes a Trial from DB.
	 * 
	 * @param trial
	 * @throws DataManagementException
	 */
	public abstract void deleteTrial(Trial trial) throws DataManagementException;

}