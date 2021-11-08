package edu.kit.exp.server.gui.structuretab;

import java.sql.Timestamp;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.kit.exp.common.Constants;
import edu.kit.exp.server.gui.DataInputException;
import edu.kit.exp.server.gui.treatment.TreatmentManagementDialogController;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Experiment;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.SequenceElement;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;
import edu.kit.exp.server.structure.CohortManagement;
import edu.kit.exp.server.structure.ExperimentManagement;
import edu.kit.exp.server.structure.PeriodManagement;
import edu.kit.exp.server.structure.SequenceElementManagement;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;

/**
 * Controller class for the structure tab.
 * 
 */
public class StructureTabController extends Observable {

	private static StructureTabController instance = new StructureTabController();
	private ExperimentManagement experimentManagement = ExperimentManagement.getInstance();
	private CohortManagement cohortManagement = CohortManagement.getInstance();
	private Experiment currentExperiment;
	private SessionManagement sessionManagement = SessionManagement.getInstance();
	private PeriodManagement periodManagement = PeriodManagement.getInstance();
	private DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.TIME_STAMP_FORMAT);
	private SequenceElementManagement sequenceElementManagement = SequenceElementManagement.getInstance();

	/**
	 * Constructor
	 */
	private StructureTabController() {

	}

	/**
	 * Returns the only instance of this class.
	 * 
	 * @return
	 */
	public static StructureTabController getInstance() {

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
	 * Set the experiment that should be displayed in ExperimentBuilder
	 * 
	 * @param experiment
	 * @throws StructureManagementException
	 */
	public void setCurrentExperiment(Experiment experiment) throws StructureManagementException {

		Experiment experimentToLoad = null;

		if (currentExperiment == null) {
			experimentToLoad = experiment;
		} else {
			experimentToLoad = experimentManagement.findExperiment(experiment.getIdExperiment());
		}

		currentExperiment = experimentToLoad;

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(experiment);
		}
	}

	public Experiment getCurrentExperiment() {
		return currentExperiment;
	}

	/**
	 * Create a new session for the current experiment.
	 * 
	 * @throws StructureManagementException
	 */
	public void createNewSession() throws StructureManagementException {

		Session result = sessionManagement.createNewSession(currentExperiment);
		currentExperiment.getSessions().add(result);

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}
	}

	/**
	 * Create a new treatment block for the given Session.
	 * 
	 * @throws StructureManagementException
	 */
	public void createNewTreatmentBlock(Session session, boolean practice) throws StructureManagementException {

		SequenceElement result = sequenceElementManagement.createNewTreatmentBlock(session, practice);
		currentExperiment = result.getSession().getExperiment();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}
	}

	/**
	 * Create a new quiz for a given session.
	 * 
	 * @param session
	 * @throws StructureManagementException
	 */
	public void createNewQuiz(Session session) throws StructureManagementException {

		SequenceElement result = sequenceElementManagement.createNewQuiz(session);
		currentExperiment = result.getSession().getExperiment();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}

	}

	/**
	 * Create a new pause for a given session.
	 * 
	 * @param session
	 * @throws StructureManagementException
	 */
	public void createNewPause(Session session) throws StructureManagementException {

		SequenceElement result = sequenceElementManagement.createNewPause(session);
		currentExperiment = result.getSession().getExperiment();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}

	}

	/**
	 * Create a new period for a given treatmentBlock.
	 * 
	 * @param treatmentBlock
	 * @param numberOfPeriods
	 * @throws StructureManagementException
	 */
	public void createNewPeriods(TreatmentBlock treatmentBlock, int numberOfPeriods) throws StructureManagementException {

		List<Period> result = periodManagement.createNewPeriods(treatmentBlock, numberOfPeriods);

		currentExperiment = result.get(0).getTreatmentBlock().getSession().getExperiment();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}

	}

	/**
	 * Updates a given experiment.
	 * 
	 * @param experiment
	 * @throws StructureManagementException
	 */
	public void updateExperiment(Experiment experiment) throws StructureManagementException {

		currentExperiment = experimentManagement.updateExperiment(experiment);

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(experiment);
		}
	}

	/**
	 * Updates the cohorts of a session.
	 * 
	 * @param session
	 *            The Session to be edited
	 * @param cohortsStr
	 *            Number of cohorts as String
	 * @param cohortSizeStr
	 *            Size of a cohort as String
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void updateCohortsOfSession(Session session, String cohortsStr, String cohortSizeStr) throws DataInputException, StructureManagementException {

		Integer numberOfCohorts;
		Integer cohortSize;

		try {
			numberOfCohorts = Integer.valueOf(cohortsStr);
			cohortSize = Integer.valueOf(cohortSizeStr);

			if (cohortSize < 1 || numberOfCohorts < 1) {
				throw new DataInputException("Please check your cohort settings!");
			}

		} catch (NumberFormatException e) {
			throw new DataInputException("Please check your cohort settings!" + e.getMessage());
		}

		if (numberOfCohorts != session.getCohorts().size() || cohortSize != session.getCohorts().get(0).getSize()) {

			Vector<Cohort> cohortVector = new Vector<Cohort>(session.getCohorts());

			for (Cohort cohort : cohortVector) {
				System.out.println("--" + cohort.toString());
				cohortManagement.deleteCohort(cohort);
			}

			Cohort cohort = null;

			for (int i = 0; i < numberOfCohorts; i++) {

				cohort = cohortManagement.createNewCohort(session, cohortSize);
				System.out.println("<<" + cohort.toString());
			}

			currentExperiment = cohort.getSession().getExperiment();
		}

	}

	/**
	 * Updates a Session
	 * 
	 * @param session
	 * @param cohortsStr
	 *            Number of cohorts as String
	 * @param cohortSizeStr
	 *            Size of a cohort as String
	 * @throws StructureManagementException
	 * @throws DataInputException
	 */
	public void updateSession(Session session, String cohortsStr, String cohortSizeStr) throws StructureManagementException, DataInputException {

		Session result = sessionManagement.updateSession(session);

		currentExperiment = result.getExperiment();

		updateCohortsOfSession(session, cohortsStr, cohortSizeStr);

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}
	}

	/**
	 * Updates a SequenceElement
	 * 
	 * @param sequenceElement
	 * @throws StructureManagementException
	 * @throws DataInputException
	 */
	public void updateSequenceElement(SequenceElement sequenceElement) throws StructureManagementException, DataInputException {

		if (sequenceElement.getClass().equals(TreatmentBlock.class)) {
			TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

			// Check for illegal state
			if (treatmentBlock.getRandomization() == null && treatmentBlock.getTreatments().size() > 1) {
				throw new DataInputException("TreatmentBlock has more than 1 Treatment but there is no randomization procedure defined!");
			}

			// Check for illegal state
			if (treatmentBlock.getRandomization() != null && treatmentBlock.getTreatments().size() == 1) {
				throw new DataInputException("Random matching is selected, but TreatmentBlock has only 1 Treatment!");
			}
		}

		SequenceElement result = sequenceElementManagement.updateSequenceElement(sequenceElement);
		currentExperiment = result.getSession().getExperiment();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}

	}

	/**
	 * Updates a period
	 * 
	 * @param period
	 * @throws StructureManagementException
	 */
	public void updatePeriod(Period period) throws StructureManagementException {

		Period result = periodManagement.updatePeriod(period);
		currentExperiment = result.getTreatmentBlock().getSession().getExperiment();

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}

	}

	/**
	 * Shows the treatment management dialog
	 */
	public void showTreatmentDialog() {

		TreatmentManagementDialogController.getInstance().showTreatmentManagementDialog();

	}

	/**
	 * Delete a session.
	 * 
	 * @param session
	 * @throws StructureManagementException
	 */
	public void removeSession(Session session) throws StructureManagementException {

		sessionManagement.deleteSession(session.getIdSession());

		currentExperiment = experimentManagement.findExperiment(currentExperiment.getIdExperiment());

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}
	}

	/**
	 * Delete a sequence element.
	 * 
	 * @param sequenceElement
	 * @throws StructureManagementException
	 */
	public void removeSequenceElement(SequenceElement sequenceElement) throws StructureManagementException {

		sequenceElementManagement.deleteSequenceElement(sequenceElement);
		currentExperiment = experimentManagement.findExperiment(currentExperiment.getIdExperiment());

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}
	}

	/**
	 * Remove a period.
	 * 
	 * @param period
	 * @throws StructureManagementException
	 */
	public void removePeriod(Period period) throws StructureManagementException {

		periodManagement.deletePeriod(period.getIdPeriod());
		currentExperiment = experimentManagement.findExperiment(currentExperiment.getIdExperiment());

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}
	}

	/**
	 * Parses a String to a TimeStamp
	 * 
	 * @param timeStamp
	 * @return
	 * @throws DataInputException
	 */
	public Timestamp parseDateString(String timeStamp) throws DataInputException {

		try {
			long millies = fmt.parseMillis(timeStamp);
			Timestamp result = new Timestamp(millies);
			return result;

		} catch (Exception e) {
			throw new DataInputException("Date could not be parsed. Format: " + Constants.TIME_STAMP_FORMAT);
		}
	}

	/**
	 * Returns the selected session.
	 * 
	 * @return
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public Session getSelectedSession() throws DataInputException, StructureManagementException {

		Session selectedSession = StructureTreeBuilder.getInstance().getSelectedSession();

		// Make sure to get actual data
		return sessionManagement.findSession(selectedSession.getIdSession());

	}

	/**
	 * Adds a treatment to the selected session.
	 * 
	 * @param newTreatment
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void addTreatmentToSelectedTreatmentBlock(Treatment newTreatment) throws DataInputException, StructureManagementException {

		TreatmentBlock treatmentBlock = StructureTreeBuilder.getInstance().getSelectedTreatmentBlock();
		treatmentBlock.getTreatments().add(newTreatment);

		TreatmentBlock result = (TreatmentBlock) sequenceElementManagement.updateSequenceElement(treatmentBlock);

		currentExperiment = result.getSession().getExperiment();

		for (Treatment t : result.getTreatments()) {
			System.out.println(t.toString());
		}

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}

	}

	/**
	 * Removes a treatment from the selected session.
	 * 
	 * @param treatment
	 * @throws DataInputException
	 * @throws StructureManagementException
	 */
	public void removeTreatmentFromSelectedTreatmentBlock(Treatment treatment) throws DataInputException, StructureManagementException {

		TreatmentBlock treatmentBlock = StructureTreeBuilder.getInstance().getSelectedTreatmentBlock();
		treatmentBlock.getTreatments().remove(treatment);

		TreatmentBlock result = (TreatmentBlock) sequenceElementManagement.updateSequenceElement(treatmentBlock);

		currentExperiment = result.getSession().getExperiment();

		for (Treatment t : result.getTreatments()) {
			System.out.println(t.toString());
		}

		if (countObservers() > 0) {
			setChanged();
			notifyObservers(currentExperiment);
		}

	}

}
