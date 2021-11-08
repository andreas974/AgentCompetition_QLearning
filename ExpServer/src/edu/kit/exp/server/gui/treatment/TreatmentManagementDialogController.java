package edu.kit.exp.server.gui.treatment;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.structuretab.StructureTabController;
import edu.kit.exp.server.jpa.dao.ExperimentDAO;
import edu.kit.exp.server.jpa.dao.IExperimentDAO;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.TreatmentManagement;

/**
 * Controller for the treatment management dialog.
 * 
 */
public class TreatmentManagementDialogController extends Observable {

	private static TreatmentManagementDialogController instance = new TreatmentManagementDialogController();
	private IExperimentDAO experimentDao = new ExperimentDAO();
	private TreatmentManagement treatmentManagement = TreatmentManagement.getInstance();
	private List<Treatment> listOfAllTreatments;

	private TreatmentManagementDialogController() {

	}

	/**
	 * Returns the only instance of this class.
	 * 
	 * @return
	 */
	public static TreatmentManagementDialogController getInstance() {

		return instance;
	}

	/**
	 * Add an observer to this controller.
	 */
	@Override
	public void addObserver(Observer o) {
		super.addObserver(o);
	}

	/**
	 * Gets all Treatments from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Treatment> getAllTreatments() throws StructureManagementException {

		if (listOfAllTreatments == null) {
			listOfAllTreatments = treatmentManagement.findAllTreatments();
		}

		return listOfAllTreatments;
	}

	/**
	 * Create a new treatment.
	 * 
	 * @param name
	 * @param description
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void createTreatment(Treatment treatment) throws DataInputException, StructureManagementException {

		Treatment result = null;

		try {
			result = treatmentManagement.createNewTreatment(treatment);
		} catch (StructureManagementException e) {

			if (e.getMessage().contains("treatment_institution_key_unique_index")) {
				throw new DataInputException("The institution factory key already exists! Please choose another one.");
			}
			if (e.getMessage().contains("treatment_environment_key_unique_index")) {
				throw new DataInputException("The environment factory key already exists! Please choose another one.");
			}

			throw e;
		}

		listOfAllTreatments.add(result);

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(listOfAllTreatments);
		}

	}

	/**
	 * Update a treatment.
	 * 
	 * @param treatment
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void updateTreatment(Treatment treatment) throws DataInputException, StructureManagementException {

		try {
			treatmentManagement.updateTreatment(treatment);

			listOfAllTreatments = treatmentManagement.findAllTreatments();

			if (countObservers() > 0) {
				setChanged();
				notifyObservers(listOfAllTreatments);
			}

		} catch (StructureManagementException e) {

			if (e.getMessage().contains("treatment_institution_key_unique_index")) {
				throw new DataInputException("The institution factory key already exists! Please choose another one.");
			} else {
				if (e.getMessage().contains("treatment_environment_key_unique_index")) {
					throw new DataInputException("The environment factory key already exists! Please choose another one.");
				} else {

					throw e;
				}
			}
		}
	}

	/**
	 * Shows treatment management dialog.
	 */
	public void showTreatmentManagementDialog() {

		TreatmentManagementDialog.getInstance().setVisible(true);

	}

	/**
	 * Shows treatment creation dialog.
	 */
	public void showTreatmentCreationDialog() {

		TreatmentUpdateDialog td = new TreatmentUpdateDialog(new Treatment(), "New Treatment");
		td.setVisible(true);

	}

	/**
	 * Shows the treatment update dialog.
	 * 
	 * @param t
	 * @throws DataInputException
	 */
	public void showTreatmentUpdateDialog(Treatment t) throws DataInputException {

		if (t == null) {
			throw new DataInputException("Please select a treatment.");
		}

		TreatmentUpdateDialog td = new TreatmentUpdateDialog(t, "Change Treatment");
		td.setVisible(true);

	}

	/**
	 * Deletes a treatment.
	 * 
	 * @param treatment
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void deleteTreatment(Treatment treatment) throws DataInputException, StructureManagementException {

		if (treatment == null) {
			DataInputException e = new DataInputException("Please select an treatment.");
			throw e;
		}

		treatmentManagement.deleteTreatment(treatment);

		listOfAllTreatments = treatmentManagement.findAllTreatments();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(listOfAllTreatments);
		}
	}

	/**
	 * Adds a Treatment to a TreatmentBlock
	 * 
	 * @param t
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void addTreatmentToTreatmentBlock(Treatment t) throws DataInputException, StructureManagementException {

		try {
			StructureTabController.getInstance().addTreatmentToSelectedTreatmentBlock(t);
		} catch (StructureManagementException e) {
			if (e.getMessage().contains("Unique-Constraint")) {
				throw new DataInputException("Treatment already added.");
			} else
				throw e;
		}
	}

	/**
	 * Removes a Treatment to a TreatmentBlock
	 * 
	 * @param t
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void removeTreatmentFromTreatmentBlock(Treatment t) throws DataInputException, StructureManagementException {

		try {
			StructureTabController.getInstance().removeTreatmentFromSelectedTreatmentBlock(t);
		} catch (StructureManagementException e) {
			if (e.getMessage().contains("Unique-Constraint")) {
				throw new DataInputException("Treatment already added.");
			} else
				throw e;
		}
	}

	public void setExperimentDao(IExperimentDAO experimentDao) {
		this.experimentDao = experimentDao;
	}

	public IExperimentDAO getExperimentDao() {
		return experimentDao;
	}
}
