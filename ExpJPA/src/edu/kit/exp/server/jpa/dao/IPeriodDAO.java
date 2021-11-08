package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Period;

/**
 * Provides CRUD functionalities for Periods in DB.
 */
public interface IPeriodDAO {

	/**
	 * Returns all Periods from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Period> findAllPeriods() throws DataManagementException;

	/**
	 * Finds a Period by PeriodId.
	 * 
	 * @param periodId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Period findPeriodById(Integer periodId) throws DataManagementException;

	/**
	 * Creates a Period in DB.
	 * 
	 * @param period
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Period createPeriod(Period period) throws DataManagementException;

	/**
	 * Updates a Period in DB.
	 * 
	 * @param period
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Period updatePeriod(Period period) throws DataManagementException;

	/**
	 * Removes a Period from DB.
	 * 
	 * @param period
	 * @throws DataManagementException
	 */
	public abstract void deletePeriod(Period period) throws DataManagementException;

}