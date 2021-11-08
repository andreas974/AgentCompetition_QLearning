package edu.kit.exp.server.gui.starttab;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.mainframe.MainFrameController;
import edu.kit.exp.server.gui.structuretab.StructureTabController;
import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.ExperimentDAO;
import edu.kit.exp.server.jpa.dao.IExperimentDAO;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.structure.ExperimentManagement;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Controller class for the start tab.
 *
 */
public class StartTabController extends Observable {

	private static StartTabController instance = new StartTabController();
	private IExperimentDAO experimentDao = new ExperimentDAO();
	private ExperimentManagement experimentManagement = ExperimentManagement.getInstance();
	private List<Experiment> listOfAllExperiments;


	private StartTabController() {

	}

	/**
	 * Returns the only instance of this class.
	 * @return
	 */
	public static StartTabController getInstance() {

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
	 * Open an experiment in order to show it in the experiment builder.
	 * @param idExperiment
	 * @throws StructureManagementException
	 * @throws DataInputException 
	 */
	public void openExperiment(Integer idExperiment) throws StructureManagementException, DataInputException {

		if (idExperiment==null) {
			DataInputException e = new DataInputException("Please select an experiment.");
			throw e;
		}
		
		Experiment experiment = experimentManagement.findExperiment(idExperiment);
		StructureTabController.getInstance().setCurrentExperiment(experiment);
		MainFrameController.getInstance().switchToTab(1);

	}

	/**
	 * Create a new experiment.
	 * 
	 * @param name
	 * @param description
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void createNewExperiment(String name, String description) throws DataInputException, StructureManagementException {

		if (name.equals("")) {
			DataInputException e = new DataInputException("Please enter an experiment name.");
			throw e;
		}

		Experiment result = experimentManagement.createExperiment(name, description);
		listOfAllExperiments.add(result);

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(listOfAllExperiments);
		}
	}
	
	public void deleteExperiment(Integer idExperiment) throws DataInputException, StructureManagementException {

		if (idExperiment==null) {
			DataInputException e = new DataInputException("Please select an experiment.");
			throw e;
		}

		experimentManagement.deleteExperiment(idExperiment);
		
		listOfAllExperiments = experimentManagement.findAllExperiments();
		

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(listOfAllExperiments);
		}
	}

	/**
	 * Get a list of all experiments
	 * @return
	 * @throws DataManagementException
	 */
	public List<Experiment> getAllExperiments() throws DataManagementException {

		if (listOfAllExperiments == null) {
			listOfAllExperiments = experimentDao.findAllExperiments();
		}

		return listOfAllExperiments;
	}

	

}
