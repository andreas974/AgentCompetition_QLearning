package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.ISequenceElementDAO;
import edu.kit.exp.server.jpa.dao.SequenceElementDAO;
import edu.kit.exp.server.jpa.entity.Pause;
import edu.kit.exp.server.jpa.entity.Quiz;
import edu.kit.exp.server.jpa.entity.SequenceElement;
import edu.kit.exp.server.jpa.entity.Session;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;

/**
 * This class provides all persistence functions of SequenceElements.
 * 
 */
public class SequenceElementManagement {

	private static SequenceElementManagement instance;
	private ISequenceElementDAO sequenceElementDAO = new SequenceElementDAO();

	/**
	 * Returns an instance of the SequenceElementManagement.
	 * 
	 * @return
	 */
	public static SequenceElementManagement getInstance() {

		if (instance == null) {
			instance = new SequenceElementManagement();
		}

		return instance;
	}

	private SequenceElementManagement() {

	}

	/**
	 * Finds a SequenceElement in DB.
	 * 
	 * @param treatmentBlockId
	 * @return
	 * @throws StructureManagementException
	 */
	public SequenceElement findSequenceElement(Integer treatmentBlockId) throws StructureManagementException {

		SequenceElement treatmentBlock;

		try {
			treatmentBlock = sequenceElementDAO.findSequenceElementById(treatmentBlockId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SequenceElement could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return treatmentBlock;
	}

	/**
	 * Creates the SequenceElement TreatmentBlock in given Session.
	 * 
	 * @param session
	 * @param practice
	 * @return
	 * @throws StructureManagementException
	 */
	public SequenceElement createNewTreatmentBlock(Session session, boolean practice) throws StructureManagementException {

		TreatmentBlock treatmentBlock = new TreatmentBlock();
		Integer sequenceNumber = session.getSequenceElements().size() + 1;
		treatmentBlock.setName("TreatmentBlock " + sequenceNumber.toString());
		treatmentBlock.setSession(session);
		treatmentBlock.setSequenceNumber(sequenceNumber);

		treatmentBlock.setPractice(practice);

		TreatmentBlock result; // FIXME WIRD DIE METHODE GENUTZT EVTL TB
								// MANAGEMENT LÖSCHEN

		try {
			result = (TreatmentBlock) sequenceElementDAO.createSequenceElement(treatmentBlock);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("TreatmentBlock could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Creates the SequenceElement Quiz in given Session.
	 * 
	 * @param session
	 * @return
	 * @throws StructureManagementException
	 */
	public Quiz createNewQuiz(Session session) throws StructureManagementException {

		Quiz quiz = new Quiz();
		quiz.setSession(session);
		Integer sequenceNumber = session.getSequenceElements().size() + 1;
		quiz.setSequenceNumber(sequenceNumber);

		Quiz result;

		try {
			result = (Quiz) sequenceElementDAO.createSequenceElement(quiz);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Quiz could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;
	}

	/**
	 * Create the SequenceElement Pause in given Session.
	 * 
	 * @param session
	 * @return
	 * @throws StructureManagementException
	 */
	public Pause createNewPause(Session session) throws StructureManagementException {

		Pause pause = new Pause();
		pause.setSession(session);
		Integer sequenceNumber = session.getSequenceElements().size() + 1;
		pause.setSequenceNumber(sequenceNumber);
		pause.setTime(60000L); //60000 milli sec
		pause.setMessage("Break... Please wait!");

		Pause result;

		try {
			result = (Pause) sequenceElementDAO.createSequenceElement(pause);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Pause could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Get all SequenceElement from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<SequenceElement> findAllTreatmentBlocks() throws StructureManagementException {

		List<SequenceElement> list;
		try {
			list = sequenceElementDAO.findAllSequenceElements();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SequenceElements could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a SequenceElement from db.
	 * 
	 * @param sequenceElement
	 * @throws StructureManagementException
	 */
	public void deleteSequenceElement(SequenceElement sequenceElement) throws StructureManagementException {

		try {
			sequenceElementDAO.deleteSequenceElement(sequenceElement);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SequenceElement could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * Updates a given SequenceElement.
	 * 
	 * @param sequenceElements
	 * @return
	 * @throws StructureManagementException
	 */
	public SequenceElement updateSequenceElement(SequenceElement sequenceElements) throws StructureManagementException {

		SequenceElement result;
		try {
			result = sequenceElementDAO.updateSequenceElement(sequenceElements);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SequenceElement could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

}
