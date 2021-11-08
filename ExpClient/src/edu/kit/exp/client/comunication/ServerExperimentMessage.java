package edu.kit.exp.client.comunication;

import edu.kit.exp.common.IScreenParamObject;

public class ServerExperimentMessage extends ServerMessage {
	public static final Integer SHOW_SCREEN = 0;
	public static final Integer SHOW_GENERAL_SCREEN = 1;
	public static final Integer RECONNECT = 2;
	public static final Integer SHOW_SCREEN_WITH_DEADLINE = 3;

	private Integer type;
	private String globalScreenId;
	private IScreenParamObject parameters;
	private String gameId;
	private Long showUpTime;

	public ServerExperimentMessage(Integer type, String globalScreenId, IScreenParamObject parameters, String gameId) {
		this(type, globalScreenId, parameters, gameId, 0l);
	}

	public ServerExperimentMessage(Integer type, String globalScreenId, IScreenParamObject parameters, String gameId, Long showUpDuration) {
		super();
		this.type = type;
		this.globalScreenId = globalScreenId;
		this.parameters = parameters;
		this.gameId = gameId;
		this.setShowUpTime(showUpDuration);
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getGlobalScreenId() {
		return globalScreenId;
	}

	public void setGlobalScreenId(String globalScreenId) {
		this.globalScreenId = globalScreenId;
	}

	public IScreenParamObject getParameters() {
		return parameters;
	}

	public void setParameters(IScreenParamObject parameters) {
		this.parameters = parameters;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String queueId) {
		this.gameId = queueId;
	}

	@Override
	public String toString() {
		return "ServerMessage [type=" + type + ", globalScreenId=" + globalScreenId + ", parameters=" + parameters + ", queueId=" + gameId + "]";
	}

	public Long getShowUpTime() {
		return showUpTime;
	}

	public void setShowUpTime(Long showUpTime) {
		this.showUpTime = showUpTime;
	}
}
