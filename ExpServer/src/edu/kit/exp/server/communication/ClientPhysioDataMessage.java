package edu.kit.exp.server.communication;

import java.util.HashMap;

public class ClientPhysioDataMessage extends ClientMessage {

	private Long localTimestamp;
	private HashMap<String, Double> data = new HashMap<String, Double>();
	
	protected ClientPhysioDataMessage(String clientId, Long timstamp, HashMap<String, Double> physioData) {
		super(clientId);
		data = physioData;
	}

	public HashMap<String, Double> getData() {
		return data;
	}

	public void setData(HashMap<String, Double> data) {
		this.data = data;
	}

	public Long getLocalTimestamp() {
		return localTimestamp;
	}

	public void setLocalTimestamp(Long localTimestamp) {
		this.localTimestamp = localTimestamp;
	}

}
