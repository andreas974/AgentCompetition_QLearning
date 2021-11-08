package edu.kit.exp.client.comunication;

import edu.kit.exp.common.IScreenParamObject;

public class ServerParamObjectUpdateMessage extends ServerMessage {

	IScreenParamObject parameterObjectUpdate;
	
	public IScreenParamObject getParameterObjectUpdate() {
		return parameterObjectUpdate;
	}

	public void setParameterObjectUpdate(IScreenParamObject parameterObjectUpdate) {
		this.parameterObjectUpdate = parameterObjectUpdate;
	}

	public ServerParamObjectUpdateMessage(IScreenParamObject parameter){
		parameterObjectUpdate = parameter;
	}

}
