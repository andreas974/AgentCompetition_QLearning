package edu.kit.exp.server.communication;

import edu.kit.exp.server.jpa.entity.Trial;

/**
 * This message is used for client side trial creation.
 *
 */
public class ClientTrialLogMessage extends ClientMessage{

	private Trial trial;
	private String gameId;

	protected ClientTrialLogMessage(String clientId) {
		super(clientId);
		
	}

	public ClientTrialLogMessage(String clientId, String gameId, String event, String screenName, String value, Long clientTimeStamp, Long serverTimeStamp) {
		super(clientId);
		
		this.setGameId(gameId);
		this.trial = new Trial();
		trial.setClientTime(clientTimeStamp);
		trial.setServerTime(serverTimeStamp);
		trial.setEvent(event);
		trial.setScreenName(screenName);
		trial.setValue(value);
		
		
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Trial getTrial() {
		return trial;
	}

	public void setTrial(Trial trial) {
		this.trial = trial;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((trial == null) ? 0 : trial.hashCode());
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
		ClientTrialLogMessage other = (ClientTrialLogMessage) obj;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (trial == null) {
			if (other.trial != null)
				return false;
		} else if (!trial.equals(other.trial))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientTrialLogMessage [trial=" + trial + ", gameId=" + gameId + ", clientId=" + clientId + "]";
	}
	
	
	
	
	
	

}
