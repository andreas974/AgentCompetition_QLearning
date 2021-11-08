package edu.kit.exp.server.communication;

import edu.kit.exp.common.IScreenParamObject;

/**
 * This Message is used for client responses.
 * 
 */
public class ClientResponseMessage extends ClientMessage {

	private IScreenParamObject parameters;
	private String gameId;
	private String screenId;
	private Long clientTimeStamp;
	private Long serverTimeStamp;

	private ClientResponseMessage(String clientId) {
		super(clientId);
	}

	/**
	 * Creates a ClientResponse
	 * 
	 * @param clientId
	 *            The id of the client.
	 * @param parameters
	 *            The Parameters send by the client screen. Must be known by the
	 *            receiving instance.
	 * @param gameId
	 *          ID of the running game which has to receive and process this message
	 * @param screenId The ID of the screen that triggered this message. 
	 * @param clientTimeStamp The time in milliseconds at which a screen triggered this message (i.e. pressing a button).
	 * @param serverTimeStamp The time in milliseconds at which this message was received by the server.
	 */
	public ClientResponseMessage(String clientId, IScreenParamObject parameters, String gameId, String screenId, Long clientTimeStamp, Long serverTimeStamp) {
		super(clientId);
		this.setParameters(parameters);
		this.gameId = gameId;
		this.screenId = screenId;
		this.clientTimeStamp = clientTimeStamp;
		this.setServerTimeStamp(serverTimeStamp);
	}

	@SuppressWarnings("unchecked")
	public <T extends IScreenParamObject> T getParameters() {
		return (T) parameters;
	}

	public void setParameters(IScreenParamObject parameters) {
		this.parameters = parameters;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String messageQueueId) {
		this.gameId = messageQueueId;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public Long getClientTimeStamp() {
		return clientTimeStamp;
	}

	public void setClientTimeStamp(Long clientTimeStamp) {
		this.clientTimeStamp = clientTimeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((clientTimeStamp == null) ? 0 : clientTimeStamp.hashCode());
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((screenId == null) ? 0 : screenId.hashCode());
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
		ClientResponseMessage other = (ClientResponseMessage) obj;
		if (clientTimeStamp == null) {
			if (other.clientTimeStamp != null)
				return false;
		} else if (!clientTimeStamp.equals(other.clientTimeStamp))
			return false;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (screenId == null) {
			if (other.screenId != null)
				return false;
		} else if (!screenId.equals(other.screenId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientResponseMessage [parameters=" + parameters + ", gameId=" + gameId + ", screenId=" + screenId + ", clientTimeStamp=" + clientTimeStamp + ", clientId=" + clientId + "]";
	}

	public Long getServerTimeStamp() {
		return serverTimeStamp;
	}

	public void setServerTimeStamp(Long serverTimeStamp) {
		this.serverTimeStamp = serverTimeStamp;
	}
	
	

	

}
