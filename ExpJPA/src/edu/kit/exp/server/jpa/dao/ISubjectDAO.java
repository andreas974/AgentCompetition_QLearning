package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Subject;

/**
 * Provides CRUD functionalities for Subjects in DB.
 */
public interface ISubjectDAO {

	/**
	 * Returns all Subjects from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Subject> findAllSubjects() throws DataManagementException;

	/**
	 * Finds a Subject by SubjectId.
	 * 
	 * @param subjectId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Subject findSubjectById(Integer subjectId) throws DataManagementException;

	/**
	 * Creates a Subject in DB.
	 * 
	 * @param subject
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Subject createSubject(Subject subject) throws DataManagementException;

	/**
	 * Creates a Subject in DB.
	 * 
	 * @param subject
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Subject updateSubject(Subject subject) throws DataManagementException;

	/**
	 * Removes a Subject from DB.
	 * 
	 * @param subject
	 * @throws DataManagementException
	 */
	public abstract void deleteSubject(Subject subject) throws DataManagementException;

}