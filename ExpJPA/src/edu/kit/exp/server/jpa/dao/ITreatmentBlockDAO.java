package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;

/**
 * Provides CRUD functionalities for TreatmentBlocks in DB.
 */
public interface ITreatmentBlockDAO {

	/**
	 * // * Returns all TreatmentBlocks from DB // * // * @return // * @throws
	 * DataManagementException //
	 */
	public abstract List<TreatmentBlock> findAllTreatmentBlocks() throws DataManagementException;

	// /**
	// * Finds an TreatmentBlock by id.
	// *
	// * @param id
	// * @return
	// * @throws DataManagementException
	// */
	public abstract TreatmentBlock findTreatmentBlockById(Integer id) throws DataManagementException;

	// /**
	// * Creates an TreatmentBlock in DB.
	// *
	// * @param treatmentBlock
	// * @return
	// * @throws DataManagementException
	// */
	public abstract TreatmentBlock createTreatmentBlock(TreatmentBlock treatmentBlock) throws DataManagementException;

	//
	// /**
	// * Creates an TreatmentBlock in DB.
	// *
	// * @param treatmentBlock
	// * @return
	// * @throws DataManagementException
	// */
	public abstract TreatmentBlock updateTreatmentBlock(TreatmentBlock treatmentBlock) throws DataManagementException;

	// /**
	// * Removes an TreatmentBlock from DB.
	// *
	// * @param treatmentBlock
	// * @throws DataManagementException
	// */
	public abstract void deleteTreatmentBlock(TreatmentBlock treatmentBlock) throws DataManagementException;
	//
}