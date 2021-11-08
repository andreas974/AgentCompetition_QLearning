package edu.kit.exp.server.structure;

import java.util.ArrayList;
import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.IPeriodDAO;
import edu.kit.exp.server.jpa.dao.PeriodDAO;
import edu.kit.exp.server.jpa.entity.Period;
import edu.kit.exp.server.jpa.entity.TreatmentBlock;

/**
 * This class provides all persistence functions of treatment blocks.
 * 
 */
public class PeriodManagement {

	private static PeriodManagement instance;
	private IPeriodDAO periodDAO = new PeriodDAO();

	/**
	 * Returns an instance of the PeriodManagement.
	 * 
	 * @return
	 */
	public static PeriodManagement getInstance() {

		if (instance == null) {
			instance = new PeriodManagement();
		}

		return instance;
	}

	private PeriodManagement() {

	}

	/**
	 * Finds a period in DB.
	 * 
	 * @param periodId
	 * @return
	 * @throws StructureManagementException
	 */
	public Period findPeriod(Integer periodId) throws StructureManagementException {

		Period period;

		try {
			period = periodDAO.findPeriodById(periodId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Period could not be found. Cause: "+e.getMessage());
			throw ex;
		}

		return period;
	}

	
	/**
	 * Creates new periods for the given TreatmentBlock
	 * @param treatmentBlock
	 * @return 
	 * @throws StructureManagementException
	 */
	public List<Period> createNewPeriods(TreatmentBlock treatmentBlock, int numberOfPeriods) throws StructureManagementException {

		List<Period> result = new ArrayList<Period>();		
		Integer startSequenceNumber = treatmentBlock.getPeriods().size()+1;		
		int endSequenceNumber = startSequenceNumber + numberOfPeriods;
		
		for (int i = startSequenceNumber; i < endSequenceNumber; i++) {
			
			Period period = new Period();
			period.setTreatmentBlock(treatmentBlock);
			period.setSequenceNumber(i);
			period.setPractice(treatmentBlock.getPractice());
			
			
			try {
				result.add(periodDAO.createPeriod(period));
			} catch (DataManagementException e) {
				StructureManagementException ex = new StructureManagementException("Period could not be created. Cause: "+e.getMessage());
				throw ex;
			}
		}
		
		return result;

	}

	/**
	 * Get all periods from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Period> findAllPeriods() throws StructureManagementException {

		List<Period> list;
		try {
			list = periodDAO.findAllPeriods();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Periods could not be found. Cause: "+e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a period from DB.
	 * @param idPeriod
	 * @throws StructureManagementException
	 */
	public void deletePeriod(Integer idPeriod) throws StructureManagementException {

		Period period;
		try {
			period = periodDAO.findPeriodById(idPeriod);
			periodDAO.deletePeriod(period);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Period could not be deleted. Cause: "+e.getMessage());
			throw ex;
		}
	}

	/**
	 * Updates a given Period. 
	 * @param period
	 * @return
	 * @throws StructureManagementException
	 */
	public Period updatePeriod(Period period) throws StructureManagementException {
		
		Period result;
		try {
			result = periodDAO.updatePeriod(period);
			
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Period could not be found. Cause: "+e.getMessage());
			throw ex;
		}
		
		return result;
		
	}
}
