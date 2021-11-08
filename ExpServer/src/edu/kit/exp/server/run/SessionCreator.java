package edu.kit.exp.server.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import edu.kit.exp.server.Constants;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Membership;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.Quiz;
import edu.kit.exp.server.jpa.entity.SequenceElement;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.Subject;
import edu.kit.exp.server.jpa.entity.SubjectGroup;
import edu.kit.exp.server.jpa.entity.Treatment;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;
import edu.kit.exp.server.microeconomicsystem.Environment;
import edu.kit.exp.server.microeconomicsystem.EnvironmentFactory;
import edu.kit.exp.server.microeconomicsystem.RoleMatcher;
import edu.kit.exp.server.microeconomicsystem.SubjectGroupMatcher;
import edu.kit.exp.server.structure.PeriodManagement;
import edu.kit.exp.server.structure.SequenceElementManagement;
import edu.kit.exp.server.structure.SessionManagement;
import edu.kit.exp.server.structure.StructureManagementException;
import edu.kit.exp.server.structure.SubjectGroupManagement;
import edu.kit.exp.server.structure.SubjectManagement;

/**
 * This Class is used in order to run a session. It provides methods to check
 * and complete the structure of a session.
 * 
 */
public class SessionCreator {

	private static SessionCreator instance;
	private SubjectGroupManagement subjectGroupManagement = SubjectGroupManagement.getInstance();
	private SubjectManagement subjectManagement = SubjectManagement.getInstance();
	private SessionManagement sessionManagement = SessionManagement.getInstance();
	private SequenceElementManagement sequenceElementManagement = SequenceElementManagement.getInstance();
	private PeriodManagement periodManagement = PeriodManagement.getInstance();
	private RandomNumberGenerator randomNumberGenerator = RandomNumberGenerator.getInstance();
	private Vector<SequenceElement> sequenceElementVector;
	private Vector<Period> periodVector;
	private int treatmentBlockCounter = 0;

	public static SessionCreator getInstance() {

		if (instance == null) {
			instance = new SessionCreator();
		}

		return instance;
	}

	private SessionCreator() {

	}

	/**
	 * This method checks if the given session is ready to be run.
	 * 
	 * @param session
	 * @throws SessionRunException
	 *             If session configuration is incomplete.
	 */
	public void checkIfRunConditionsMet(Session session) throws SessionRunException {

		List<Cohort> cohortList = session.getCohorts();
		List<SequenceElement> sequenceElementList = session.getSequenceElements();
		Collections.sort(sequenceElementList);
		int lastSequenceNumber = 0;
		int treatmentBlockCounter = 0;

		// Check Cohorts
		if (cohortList.size() < 1) {
			throw new SessionRunException("No cohorts defined for session! " + session.toString());
		}

		// Check TreatmentBlocks and SequenceElements
		if (sequenceElementList.size() < 1) {
			throw new SessionRunException("No Sequence Elements defined for session! " + session.toString());
		}

		for (SequenceElement sequenceElement : sequenceElementList) {

			if (sequenceElement.getSequenceNumber().equals(lastSequenceNumber)) {
				throw new SessionRunException("Double sequence number! " + sequenceElement.toString());
			}

			lastSequenceNumber = sequenceElement.getSequenceNumber();

			if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

				treatmentBlockCounter++;
				TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

				if (treatmentBlock.getTreatments().isEmpty()) {
					throw new SessionRunException("No Treatment defined for TreatmentBlock: " + treatmentBlock.toString());
				}

				if (treatmentBlock.getPeriods().size() < 1) {
					throw new SessionRunException("No Periods defined for TreatmentBlock: " + treatmentBlock.toString());
				}
			}
			
			if (sequenceElement.getClass().equals(Quiz.class)) {
				Quiz quiz = (Quiz) sequenceElement;
				
				if (quiz.getQuizFactoryKey()==null||quiz.getQuizFactoryKey().equals("")) {
					throw new SessionRunException("No quiz factory key defined: " + quiz.toString());
				}
			}
		}

		if (treatmentBlockCounter < 1) {
			throw new SessionRunException("No TreatmentBlocks defined for Session: " + session.toString());
		}
		
		
	}

	/**
	 * Create the Subjects for each cohort.
	 * 
	 * @return
	 * @throws StructureManagementException
	 * @throws SessionRunException 
	 */
	public Session createSubjects(Session session) throws SessionRunException {

		try {
//		
		
		for (Cohort cohort : session.getCohorts()) {

			List<Subject> subjects = cohort.getSubjects();

			for (Subject subject : subjects) {
				subjectManagement.deleteSubject(subject);
			}

			subjectManagement.createNewSubjects(cohort, cohort.getSize());
			session = sessionManagement.findSession(session.getIdSession());
		}
		
		
		} catch (StructureManagementException e) {
			throw new SessionRunException("Can´t create Subjects! " + e.getMessage());
		}
		
		return session;		
	}

	/**
	 * For every TreatmentBlock in a session the periods are automatically
	 * matched to treatments.
	 * 
	 * @param session
	 * @return
	 * @throws SessionRunException
	 */
	public Session matchPeriodsToTreatments(Session session) throws SessionRunException {

		sequenceElementVector = new Vector<SequenceElement>(session.getSequenceElements());

		try {

			for (SequenceElement sequenceElement : sequenceElementVector) {

				// Possible: Cross-Over-Design, Between-Subject-Design or
				// Within-Subject-Design
				if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

					TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

					if (treatmentBlock.getRandomization() == null && treatmentBlock.getTreatments().size() == 1) {

						periodVector = new Vector<Period>(treatmentBlock.getPeriods());

						for (Period period : periodVector) {
							period.setTreatment(treatmentBlock.getTreatments().get(0));
							periodManagement.updatePeriod(period);
						}
					}

					// Check for illegal state
					if (treatmentBlock.getRandomization() == null && treatmentBlock.getTreatments().size() > 1) {
						throw new SessionRunException("Can not match periods to treatments. TreatmentBlock has more than 1 Treatment but there is no randomization procedure defined!");
					}

					// Check for illegal state
					if (treatmentBlock.getRandomization() != null && treatmentBlock.getTreatments().size() == 1) {
						throw new SessionRunException("Can not match periods to treatments randomly. TreatmentBlock has only 1 Treatment!");
					}

					// Possible: Completely-Randomized-Design or
					// Factorial-Design
					if (treatmentBlock.getRandomization() != null && treatmentBlock.getTreatments().size() > 1) {

						if (treatmentBlock.getRandomization().equals(Constants.DRAW_WITH_REPLACEMENT)) {
							matchPeriodsToTreatmentsCR(treatmentBlock);
						}

						if (treatmentBlock.getRandomization().equals(Constants.DRAW_WITHOUT_REPLACEMENT)) {
							matchPeriodsToTreatmentsFactorial(treatmentBlock);
						}
					}

					// sequence element is not a treatment block
				} else {
					continue;
				}
			}

			session = sessionManagement.findSession(session.getIdSession());

		} catch (StructureManagementException e) {
			throw new SessionRunException("Can not match periods to treatments. " + e.getMessage());
		}

		return session;
	}

	/**
	 * This Method matches the periods of a block to its defined treatments. The
	 * matching corresponds to a draw from a bowl with all possible treatments
	 * with replacement.
	 * 
	 * @param treatmentBlock
	 * @throws StructureManagementException
	 * @throws SessionRunException
	 */
	private void matchPeriodsToTreatmentsCR(TreatmentBlock treatmentBlock) throws StructureManagementException, SessionRunException {

		int num = treatmentBlock.getPeriods().size();
		int max = treatmentBlock.getTreatments().size() - 1;
		int min = 0;

		ArrayList<Integer> randomNumbers;
		List<Treatment> treatments = treatmentBlock.getTreatments();

		try {
			randomNumbers = randomNumberGenerator.generateRepeatingIntegers(num, min, max, 10);
		} catch (RandomGeneratorException e) {
			throw new SessionRunException("Can not match periods to treatments. " + e.getMessage());
		}

		periodVector = new Vector<Period>(treatmentBlock.getPeriods());
		Period period = null;
		int randomTreatmentNumber;

		for (int index = 0; index < periodVector.size(); index++) {

			period = periodVector.get(index);
			randomTreatmentNumber = randomNumbers.get(index);
			period.setTreatment(treatments.get(randomTreatmentNumber));
			Period result = periodManagement.updatePeriod(period);
			System.out.println("bam..." + result.getTreatment().toString());
		}
	}

	/**
	 * This Method matches the periods of a block to its defined treatments. The
	 * matching corresponds to a draw from a bowl with all possible treatments
	 * and their copies without replacement.
	 * 
	 * @param treatmentBlock
	 * @throws SessionRunException
	 *             If the number of periods is not a multiple of the number of
	 *             treatments.
	 * @throws StructureManagementException
	 */
	private void matchPeriodsToTreatmentsFactorial(TreatmentBlock treatmentBlock) throws SessionRunException, StructureManagementException {

		int numberOfPeriods = treatmentBlock.getPeriods().size();
		int max = numberOfPeriods - 1;
		int min = 0;
		int numberOfTreatments = treatmentBlock.getTreatments().size();

		// Check for illegal state
		int rest = numberOfPeriods % numberOfTreatments;
		if (rest != 0) {
			throw new SessionRunException("Can not match periods to treatments. Number of periods has to be a multiple of the number of treatments! Caused by:" + treatmentBlock.getName() + ", Periods: " + treatmentBlock.getPeriods().size());
		}

		int periodsPerTreatment = numberOfPeriods / numberOfTreatments;

		ArrayList<Object> pot = new ArrayList<Object>();
		List<Treatment> treatments = treatmentBlock.getTreatments();

		// Add treatment copies to pot
		for (Treatment treatment : treatments) {

			for (int i = 0; i < periodsPerTreatment; i++) {
				pot.add(treatment);
			}
		}

		ArrayList<Integer> randomNumbers;

		try {
			randomNumbers = randomNumberGenerator.generateNonRepeatingIntegers(min, max);
		} catch (RandomGeneratorException e) {
			throw new SessionRunException("Can not match periods to treatments. " + e.getMessage());
		}

		periodVector = new Vector<Period>(treatmentBlock.getPeriods());
		Period period = null;
		int treatmentCopyNumber;

		for (int index = 0; index < periodVector.size(); index++) {

			period = periodVector.get(index);
			treatmentCopyNumber = randomNumbers.get(index);
			period.setTreatment((Treatment) pot.get(treatmentCopyNumber));
			@SuppressWarnings("unused")
			Period result = periodManagement.updatePeriod(period);
		}
	}

	/**
	 * This method takes the cohorts of a session and defines which subject of a
	 * cohort interact in a certain period (SubjectGroup creation).
	 * 
	 * @param session
	 * @return
	 * @throws ExistingDataException
	 * @throws SessionRunException
	 */
	public Session defineSubjectGroups(Session session) throws ExistingDataException, SessionRunException {

		try {
			deleteOldSubjectGroups(session);
		} catch (StructureManagementException e1) {
			throw new SessionRunException("Can not delete old subject groups. " + e1.getCause());
		}

		sequenceElementVector = new Vector<SequenceElement>(session.getSequenceElements());

		Environment environment;
		RoleMatcher roleMatcher = null;
		SubjectGroupMatcher subjectGroupMatcher = null;

		for (Cohort cohort : session.getCohorts()) {

			treatmentBlockCounter = 0;

			try {

				for (SequenceElement sequenceElement : sequenceElementVector) {

					if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

						treatmentBlockCounter++;
						TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

						periodVector = new Vector<Period>(treatmentBlock.getPeriods());
						//Random matching can destroy order!
						Collections.sort(periodVector);

						@SuppressWarnings("unused")
						Treatment treatment;
						List<Subject> subjectList;
						List<Subject> matchedSubjects;
						List<SubjectGroup> resultList = null;

						for (int i = 0; i < periodVector.size(); i++) {
							resultList = null;
							Period period = periodVector.get(i);

							treatment = period.getTreatment();

							environment = EnvironmentFactory.createEnvironment(period.getTreatment().getEnvironmentFactoryKey());

							if ((period.getSequenceNumber() == 1 && treatmentBlockCounter == 1) || (environment.getResetMatchersAfterTreatmentBlocks())) {
								roleMatcher = environment.getRoleMatcher();
								subjectGroupMatcher = environment.getSubjectGroupMatcher();
							}

							subjectList = cohort.getSubjects();

							// setup only at period 1 in treatmentBlock 1 Or
							// every period 1 if flag is set
							if ((period.getSequenceNumber() == 1 && treatmentBlockCounter==1) || ( period.getSequenceNumber() == 1 && environment.getResetMatchersAfterTreatmentBlocks())) {
								matchedSubjects = roleMatcher.setupSubjectRoles(subjectList);
								resultList = subjectGroupMatcher.setupSubjectGroups(period, matchedSubjects);
							} else {
								matchedSubjects = roleMatcher.rematch(period, subjectList);
								resultList = subjectGroupMatcher.rematch(period, matchedSubjects);
							}
							if (resultList != null) {
								for (SubjectGroup subjectGroup : resultList) {
									subjectGroupManagement
											.createNewSubjectGroup(subjectGroup);

								}

								for (SubjectGroup subjectGroup : resultList) {
									System.out.println("-- SG --- ");

									for (Membership mem : subjectGroup
											.getMemberships()) {
										System.out.println(mem.toString());
									}

								}
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new SessionRunException("Error during SubjectGroup definition! Check the factory keys! " + e.getMessage());				
			}
		}

		return session;

	}

	
	/**
	 * Deletes old subjects of a session that should be reinitialized.
	 * @param session
	 * @throws StructureManagementException
	 */
	private void deleteOldSubjectGroups(Session session) throws StructureManagementException {

		Vector<SequenceElement> sequenceElementVector = new Vector<SequenceElement>(session.getSequenceElements());
		Collections.sort(sequenceElementVector);
		Vector<Period> periodVector;

		for (SequenceElement sequenceElement : sequenceElementVector) {

			if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

				TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

				periodVector = new Vector<Period>(treatmentBlock.getPeriods());

				for (Period period : periodVector) {

					Vector<SubjectGroup> subjectGroupVector = new Vector<SubjectGroup>(period.getSubjectGroups());

					for (SubjectGroup subjectGroup : subjectGroupVector) {
						subjectGroupManagement.deleteSubjectGroup(subjectGroup.getIdSubjectGroup());
					}

				}
			}
		}

	}

	/**
	 * This method resets all doneFlags! In case of session reset all done flags have to be reseted. 
	 * @param session
	 * @return
	 * @throws StructureManagementException
	 */
	public Session resetTreatmentElementFlags(Session session) throws StructureManagementException {

		Vector<SequenceElement> sequenceElementVector = new Vector<SequenceElement>(session.getSequenceElements());
		Collections.sort(sequenceElementVector);
		Vector<Period> periodVector;
		@SuppressWarnings("unused")
		SequenceElement result = null;

		for (SequenceElement sequenceElement : sequenceElementVector) {
			sequenceElement.setDone(false);
			result = sequenceElementManagement.updateSequenceElement(sequenceElement);

			if (sequenceElement.getClass().equals(TreatmentBlock.class)) {

				TreatmentBlock treatmentBlock = (TreatmentBlock) sequenceElement;

				periodVector = new Vector<Period>(treatmentBlock.getPeriods());

				for (Period period : periodVector) {
					period.setDone(false);
					periodManagement.updatePeriod(period);
				}
			}

		}

		session = sessionManagement.findSession(session.getIdSession());

		return session;
	}
}
