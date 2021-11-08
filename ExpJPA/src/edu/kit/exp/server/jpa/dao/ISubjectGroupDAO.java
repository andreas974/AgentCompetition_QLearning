package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.SubjectGroup;

/**
 * Provides CRUD functionalities for SubjectGroups in DB.
 */
public interface ISubjectGroupDAO {

	/**
	 * Returns all SubjectGroups from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<SubjectGroup> findAllSubjectGroups() throws DataManagementException;

	/**
	 * Finds a SubjectGroup by SubjectGroupId.
	 * 
	 * @param subjectGroupId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract SubjectGroup findSubjectGroupById(Long subjectGroupId) throws DataManagementException;

	/**
	 * Creates a SubjectGroup in DB.
	 * 
	 * @param subjectGroup
	 * @return
	 * @throws DataManagementException
	 */
	public abstract SubjectGroup createSubjectGroup(SubjectGroup subjectGroup) throws DataManagementException;

	/**
	 * Creates a SubjectGroup in DB.
	 * 
	 * @param subjectGroup
	 * @return
	 * @throws DataManagementException
	 */
	public abstract SubjectGroup updateSubjectGroup(SubjectGroup subjectGroup) throws DataManagementException;

	/**
	 * Removes a SubjectGroup from DB.
	 * 
	 * @param subjectGroup
	 * @throws DataManagementException
	 */
	public abstract void deleteSubjectGroup(SubjectGroup subjectGroup) throws DataManagementException;

}