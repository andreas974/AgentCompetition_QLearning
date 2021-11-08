package edu.kit.exp.client.comunication;

import java.util.List;

import edu.kit.exp.common.PhysioControlCommand;

public class ServerPhysioCommandMessage extends ServerMessage {
	private PhysioControlCommand controlCommand;
	
	private List<String> sensorList;
	
	public List<String> getSensorList() {
		return sensorList;
	}

	public void setSensorList(List<String> sensorList) {
		this.sensorList = sensorList;
	}

	public ServerPhysioCommandMessage(PhysioControlCommand command, List<String> sensorList){
		controlCommand = command;
		this.sensorList = sensorList;
	}
	
	public PhysioControlCommand getCommand() {
		return controlCommand;
	}
}
