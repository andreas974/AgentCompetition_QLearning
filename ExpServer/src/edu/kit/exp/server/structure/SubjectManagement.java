package edu.kit.exp.server.structure;

import java.util.ArrayList;
import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.SubjectDAO;
import edu.kit.exp.server.jpa.entity.Cohort;
import edu.kit.exp.server.jpa.entity.Subject;

/**
 * This class provides all persistence functions of subjects.
 * 
 */
public class SubjectManagement {

	private static SubjectManagement instance;
	private SubjectDAO subjectDAO = new SubjectDAO();

	/**
	 * Returns an instance of the SubjectManagement.
	 * 
	 * @return
	 */
	public static SubjectManagement getInstance() {

		if (instance == null) {
			instance = new SubjectManagement();
		}

		return instance;
	}

	private SubjectManagement() {

	}

	/**
	 * Finds a Subject in DB.
	 * 
	 * @param subjectId
	 * @return
	 * @throws StructureManagementException
	 */
	public Subject findSubject(Integer subjectId) throws StructureManagementException {

		Subject treatmentBlock;

		try {
			treatmentBlock = subjectDAO.findSubjectById(subjectId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Subject could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return treatmentBlock;
	}

	/**
	 * Creates new Subjects for the given Cohort.
	 * 
	 * @param cohort
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Subject> createNewSubjects(Cohort cohort, int number) throws StructureManagementException {

		List<Subject> resultList = new ArrayList<Subject>();
		
		for (int i = 0; i < number; i++) {

			Subject subject = new Subject();
			subject.setCohort(cohort);

			Subject result;

			try {
				result = subjectDAO.createSubject(subject);
			} catch (DataManagementException e) {
				StructureManagementException ex = new StructureManagementException("Subject could not be created. Cause: " + e.getMessage());
				throw ex;
			}			
			resultList.add(result);
		}
		return resultList;
	}

	/**
	 * Get all Subjects from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Subject> findAllSubjects() throws StructureManagementException {

		List<Subject> list;
		try {
			list = subjectDAO.findAllSubjects();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Subjects could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a subject from DB.
	 * 
	 * @param idSubject
	 * @throws StructureManagementException
	 */
	public void deleteSubject(Subject subject) throws StructureManagementException {

		try {
			subjectDAO.deleteSubject(subject);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Subject could not be removed. Cause: " + e.getMessage());
			throw ex;
		}
	}
	
	public void deleteSubjects(Cohort cohort, int rem) throws StructureManagementException {
		
		List<Subject> list = cohort.getSubjects();
		
		for (int i = 0; i < rem; i++) {
			try {
				subjectDAO.deleteSubject(list.get(i));
			} catch (Exception e) {
				StructureManagementException ex = new StructureManagementException("Subject could not be removed. Cause: " + e.getMessage());
				throw ex;
			}
		}
		
	}

	/**
	 * Updates a given Subject.
	 * 
	 * @param treatmentBlock
	 * @return
	 * @throws StructureManagementException
	 */
	public Subject updateSubject(Subject subject) throws StructureManagementException {

		Subject result;
		try {
			result = subjectDAO.updateSubject(subject);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Subject could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	
}
