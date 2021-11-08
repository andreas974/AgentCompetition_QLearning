package edu.kit.exp.server.communication;

import edu.kit.exp.common.communication.CommunicationManager;

public class ServerCommunicationManager extends CommunicationManager<ClientMessage, ServerMessageReceiver, ServerMessageSender> {
	private static ServerCommunicationManager instance;
	
	public synchronized static ServerCommunicationManager getInstance() {

		if (instance == null) {
			instance = new ServerCommunicationManager();
			instance.messageSender = new ServerMessageSender();
			instance.messageReceiver = new ServerMessageReceiver();
		}
		return instance;
	}
	
	private ServerCommunicationManager(){
	}
}
