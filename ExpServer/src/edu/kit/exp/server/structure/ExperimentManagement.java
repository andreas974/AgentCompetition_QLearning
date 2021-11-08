package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.ExperimentDAO;
import edu.kit.exp.server.jpa.dao.IExperimentDAO;
import edu.kit.exp.server.jpa.entity.Experiment;

/**
 * This class provides all persistence functions for experiments.
 * 
 */
public class ExperimentManagement {

	private static ExperimentManagement instance;
	private IExperimentDAO experimentDAO = new ExperimentDAO();

	/**
	 * Returns an instance of the ExperimentManagement.
	 * 
	 * @return
	 */
	public static ExperimentManagement getInstance() {

		if (instance == null) {
			instance = new ExperimentManagement();
		}

		return instance;
	}

	private ExperimentManagement() {

	}

	/**
	 * Finds an experiment in DB.
	 * 
	 * @param experimentId
	 * @return
	 * @throws DataManagementException
	 */
	public Experiment findExperiment(Integer experimentId) throws StructureManagementException {

		Experiment experiment;

		try {
			experiment = experimentDAO.findExperimentById(experimentId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Experiment could not be loaded. Cause:");
			throw ex;
		}

		return experiment;
	}

	/**
	 * Creates an experiment.
	 * 
	 * @param name
	 * @param description
	 * @throws StructureManagementException
	 */
	public Experiment createExperiment(String name, String description) throws StructureManagementException {

		Experiment experiment = new Experiment();
		experiment.setName(name);
		experiment.setDescription(description);

		Experiment result;

		try {
			result = experimentDAO.createExperiment(experiment);
		} catch (DataManagementException ex) {
			StructureManagementException e = new StructureManagementException("Experiment could not be created. Cause:");
			throw e;
		}

		return result;

	}

	/**
	 * Get all experiments from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Experiment> findAllExperiments() throws StructureManagementException {

		List<Experiment> list;
		try {
			list = experimentDAO.findAllExperiments();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Experiments could not be found. Cause:");
			throw ex;
		}

		return list;
	}

	/**
	 * Delete an experiment from DB.
	 * @param idExperiment
	 * @throws StructureManagementException
	 */
	public void deleteExperiment(Integer idExperiment) throws StructureManagementException {

		Experiment exp;
		try {
			exp = experimentDAO.findExperimentById(idExperiment);
			experimentDAO.deleteExperiment(exp);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Experiments could not be found. Cause:");
			throw ex;
		}
	}

	/**
	 * Update an experiment in DB.
	 * @param experiment
	 * @return
	 * @throws StructureManagementException
	 */
	public Experiment updateExperiment(Experiment experiment) throws StructureManagementException {
		
		Experiment exp;
		try {
			exp = experimentDAO.updateExperiment(experiment);
			
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Experiments could not be found. Cause:");
			throw ex;
		}
		
		return exp;
		
	}
}
