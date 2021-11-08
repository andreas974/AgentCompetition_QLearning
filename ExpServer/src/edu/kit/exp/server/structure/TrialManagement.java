package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.ITrialDAO;
import edu.kit.exp.server.jpa.dao.TrialDAO;
import edu.kit.exp.server.jpa.entity.Trial;

/**
 * This class provides all persistence functions of trials.
 * 
 */
public class TrialManagement {

	private static TrialManagement instance;
	private ITrialDAO trialDAO = new TrialDAO();

	/**
	 * Returns an instance of the TrialManagement.
	 * 
	 * @return
	 */
	public static TrialManagement getInstance() {

		if (instance == null) {
			instance = new TrialManagement();
		}

		return instance;
	}

	private TrialManagement() {

	}

	/**
	 * Finds a trial in DB.
	 * 
	 * @param trialId
	 * @return
	 * @throws StructureManagementException
	 */
	public Trial findTrial(Long trialId) throws StructureManagementException {

		Trial trial;

		try {
			trial = trialDAO.findTrialById(trialId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Trial could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return trial;
	}

	/**
	 * Creates a new trial for the given Experiment
	 * 
	 * @param experiment
	 * @return
	 * @throws StructureManagementException
	 */
	public Trial createNewTrial(Trial trial) throws StructureManagementException {

		Trial result;

		try {
			result = trialDAO.createTrial(trial);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Trial could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Get all trials from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Trial> findAllTrials() throws StructureManagementException {

		List<Trial> list;
		try {
			list = trialDAO.findAllTrials();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Trials could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a trial from DB.
	 * 
	 * @param idTrial
	 * @throws StructureManagementException
	 */
	public void deleteTrial(Long idTrial) throws StructureManagementException {

		Trial trial;
		try {
			trial = trialDAO.findTrialById(idTrial);
			trialDAO.deleteTrial(trial);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Trials could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * Updates a given trial.
	 * @param trial
	 * @return
	 * @throws StructureManagementException
	 */
	public Trial updateTrial(Trial trial) throws StructureManagementException {

		Trial result;

		try {
			result = trialDAO.updateTrial(trial);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Trials could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
