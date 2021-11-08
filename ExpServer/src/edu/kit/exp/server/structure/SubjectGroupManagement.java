package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.ISubjectGroupDAO;
import edu.kit.exp.server.jpa.dao.SubjectGroupDAO;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * This class provides all persistence functions of subjectGroups.
 * 
 */
public class SubjectGroupManagement {

	private static SubjectGroupManagement instance;
	private ISubjectGroupDAO subjectGroupDAO = new SubjectGroupDAO();

	/**
	 * Returns an instance of the SubjectGroupManagement.
	 * 
	 * @return
	 */
	public static SubjectGroupManagement getInstance() {

		if (instance == null) {
			instance = new SubjectGroupManagement();
		}

		return instance;
	}

	private SubjectGroupManagement() {

	}

	/**
	 * Finds a subjectGroup in DB.
	 * 
	 * @param subjectGroupId
	 * @return
	 * @throws StructureManagementException
	 */
	public SubjectGroup findSubjectGroup(Long subjectGroupId) throws StructureManagementException {

		SubjectGroup subjectGroup;

		try {
			subjectGroup = subjectGroupDAO.findSubjectGroupById(subjectGroupId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroup could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return subjectGroup;
	}

	/**
	 * Creates a new subjectGroup.
	 * 
	 * @param experiment
	 * @return
	 * @throws StructureManagementException
	 */
	public SubjectGroup createNewSubjectGroup(SubjectGroup subjectGroup) throws StructureManagementException {

		SubjectGroup result;

		try {
			result = subjectGroupDAO.createSubjectGroup(subjectGroup);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroup could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Get all subjectGroups from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<SubjectGroup> findAllSubjectGroups() throws StructureManagementException {

		List<SubjectGroup> list;
		try {
			list = subjectGroupDAO.findAllSubjectGroups();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroups could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a subjectGroup from DB.
	 * 
	 * @param idSubjectGroup
	 * @throws StructureManagementException
	 */
	public void deleteSubjectGroup(Long idSubjectGroup) throws StructureManagementException {

		SubjectGroup subjectGroup;
		try {
			subjectGroup = subjectGroupDAO.findSubjectGroupById(idSubjectGroup);
			subjectGroupDAO.deleteSubjectGroup(subjectGroup);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroups could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	/**
	 * Updates a subjectGroup
	 * @param subjectGroup
	 * @return
	 * @throws StructureManagementException
	 */
	public SubjectGroup updateSubjectGroup(SubjectGroup subjectGroup) throws StructureManagementException {

		SubjectGroup result;

		try {
			result = subjectGroupDAO.updateSubjectGroup(subjectGroup);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("SubjectGroups could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
