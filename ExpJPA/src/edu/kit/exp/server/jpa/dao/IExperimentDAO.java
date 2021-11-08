package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Experiment;

/**
 * Provides CRUD functionalities for Experiments in DB.
 */
public interface IExperimentDAO {

	/**
	 * Returns all experiments from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Experiment> findAllExperiments() throws DataManagementException;

	/**
	 * Finds an experiment by experimentId.
	 * 
	 * @param experimentId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Experiment findExperimentById(Integer experimentId) throws DataManagementException;

	/**
	 * Finds an experiment by name.
	 * 
	 * @param experimentName
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Experiment> findExperimentsByName(String experimentName) throws DataManagementException;

	/**
	 * Creates an experiment in DB.
	 * 
	 * @param experiment
	 * @throws DataManagementException
	 */
	public abstract Experiment createExperiment(Experiment experiment) throws DataManagementException;

	/**
	 * Creates an experiment in DB.
	 * 
	 * @param experiment
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Experiment updateExperiment(Experiment experiment) throws DataManagementException;

	/**
	 * Removes an experiment from DB.
	 * 
	 * @param experiment
	 * @throws DataManagementException
	 */
	public abstract void deleteExperiment(Experiment experiment) throws DataManagementException;

}