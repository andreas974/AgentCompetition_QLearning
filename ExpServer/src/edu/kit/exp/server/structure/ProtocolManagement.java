package edu.kit.exp.server.structure;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.dao.IProtocolDAO;
import edu.kit.exp.server.jpa.dao.ProtocolDAO;
import edu.kit.exp.server.jpa.entity.Protocol;

/**
 * This class provides all persistence functions of protocols.
 * 
 */
public class ProtocolManagement {

	private static ProtocolManagement instance;
	private IProtocolDAO protocolDAO = new ProtocolDAO();
	
	/**
	 * Returns an instance of the ProtocolManagement.
	 * 
	 * @return
	 */
	public static ProtocolManagement getInstance() {

		if (instance == null) {
			instance = new ProtocolManagement();
		}

		return instance;
	}

	private ProtocolManagement() {

	}

	/**
	 * Finds a protocol in DB.
	 * 
	 * @param protocolId
	 * @return
	 * @throws StructureManagementException
	 */
	public Protocol findProtocol(Integer protocolId) throws StructureManagementException {

		Protocol protocol;

		try {
			protocol = protocolDAO.findProtocolById(protocolId);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Protocol could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return protocol;
	}

	/**
	 * Creates a new protocol for the given Experiment
	 * 
	 * @param experiment
	 * @return
	 * @throws StructureManagementException
	 */
	public Protocol createNewProtocol(Protocol protocol) throws StructureManagementException {

		
		Protocol result;

		try {
			result = protocolDAO.createProtocol(protocol);
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Protocol could not be created. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}

	/**
	 * Get all protocols from DB.
	 * 
	 * @return
	 * @throws StructureManagementException
	 */
	public List<Protocol> findAllProtocols() throws StructureManagementException {

		List<Protocol> list;
		try {
			list = protocolDAO.findAllProtocols();
		} catch (DataManagementException e) {
			StructureManagementException ex = new StructureManagementException("Protocols could not be found. Cause: " + e.getMessage());
			throw ex;
		}

		return list;
	}

	/**
	 * Removes a protocol from DB.
	 * 
	 * @param idProtocol
	 * @throws StructureManagementException
	 */
	public void deleteProtocol(Integer idProtocol) throws StructureManagementException {

		Protocol protocol;
		try {
			protocol = protocolDAO.findProtocolById(idProtocol);
			protocolDAO.deleteProtocol(protocol);
		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Protocols could not be found. Cause: " + e.getMessage());
			throw ex;
		}
	}

	
	public Protocol updateProtocol(Protocol protocol) throws StructureManagementException {

		Protocol result;

		try {
			result = protocolDAO.updateProtocol(protocol);

		} catch (Exception e) {
			StructureManagementException ex = new StructureManagementException("Protocols could not be updated. Cause: " + e.getMessage());
			throw ex;
		}

		return result;

	}
}
