package edu.kit.exp.server.jpa.dao;

import java.util.List;

import edu.kit.exp.server.jpa.DataManagementException;
import edu.kit.exp.server.jpa.entity.Protocol;

/**
 * Provides CRUD functionalities for Protocolls in DB.
 */
public interface IProtocolDAO {

	/**
	 * Returns all protocolls from DB
	 * 
	 * @return
	 * @throws DataManagementException
	 */
	public abstract List<Protocol> findAllProtocols() throws DataManagementException;

	/**
	 * Finds a protocol by id.
	 * 
	 * @param protocolId
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Protocol findProtocolById(Integer protocolId) throws DataManagementException;

	/**
	 * Updates a protocoll in DB.
	 * 
	 * @param protocol
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Protocol updateProtocol(Protocol protocol) throws DataManagementException;

	/**
	 * Creates a protocol in DB.
	 * 
	 * @param protocol
	 * @return
	 * @throws DataManagementException
	 */
	public abstract Protocol createProtocol(Protocol protocol) throws DataManagementException;

	/**
	 * Removes a protocol from DB.
	 * 
	 * @param protocol
	 * @throws DataManagementException
	 */
	public abstract void deleteProtocol(Protocol protocol) throws DataManagementException;

}