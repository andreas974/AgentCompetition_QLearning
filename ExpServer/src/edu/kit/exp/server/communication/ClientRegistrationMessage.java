package edu.kit.exp.server.communication;

import edu.kit.exp.common.IClient;

/**
 * This Message is used to identify a client registration at the server.
 * 
 */
public class ClientRegistrationMessage extends ClientMessage {

	private IClient clientRemoteObject;

	private ClientRegistrationMessage(String clientId) {
		super(clientId);
	}

	/**
	 * 
	 * @param clientId
	 *            The id of the client.
	 * @param clientRemoteObject
	 *            The remote handler send with the registration request.
	 */
	public ClientRegistrationMessage(String clientId, IClient clientRemoteObject) {
		super(clientId);
		this.setClientRemoteObject(clientRemoteObject);
	}

	public IClient getClientRemoteObject() {
		return clientRemoteObject;
	}

	public void setClientRemoteObject(IClient clientRemoteObject) {
		this.clientRemoteObject = clientRemoteObject;
	}

	@Override
	public String toString() {
		return "ClientRegistrationMessage [clientId=" + clientId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((clientRemoteObject == null) ? 0 : clientRemoteObject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientRegistrationMessage other = (ClientRegistrationMessage) obj;
		if (clientRemoteObject == null) {
			if (other.clientRemoteObject != null)
				return false;
		} else if (!clientRemoteObject.equals(other.clientRemoteObject))
			return false;
		return true;
	}

}
