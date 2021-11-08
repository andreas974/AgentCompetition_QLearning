package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.SequenceElement;

/**
 * Provides CRUD functionalities for SequenceElements in DB.
 */
public interface ISequenceElementDAO {

	/**
	 * Finds all SequenceElements in DB.
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<SequenceElement> findAllSequenceElements() throws DataManagementException;

	/**
	 * Finds a Sequence Element by its ID from DB.
	 * 
	 * @param sequenceElementId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract SequenceElement findSequenceElementById(Integer sequenceElementId) throws DataManagementException;

	/**
	 * Update Sequence Elements in DB.
	 * 
	 * @param sequenceElements
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<SequenceElement> updateSequenceElement(List<SequenceElement> sequenceElements) throws DataManagementException;

	/**
	 * Deletes a Sequence Element from DB.
	 * 
	 * @param sequenceElement
	 * @throws DataManagementException
	 */
	public abstract void deleteSequenceElement(SequenceElement sequenceElement) throws DataManagementException;

	/**
	 * Creates an element of a subclass of Sequence Element in DB.
	 * 
	 * @param sequenceElement
	 * @return
	 * @throws DataManagementException
	 */
	public abstract SequenceElement createSequenceElement(SequenceElement sequenceElement) throws DataManagementException;

	/**
	 * Updates a Sequence Element in DB.
	 * 
	 * @param sequenceElement
	 * @return
	 * @throws DataManagementException
	 */
	public abstract SequenceElement updateSequenceElement(SequenceElement sequenceElement) throws DataManagementException;

}