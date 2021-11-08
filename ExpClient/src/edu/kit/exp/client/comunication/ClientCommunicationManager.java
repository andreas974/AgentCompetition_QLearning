package edu.kit.exp.client.comunication;

import edu.kit.exp.common.communication.CommunicationManager;

public class ClientCommunicationManager extends CommunicationManager<ServerMessage, ClientMessageReceiver, ClientMessageSender> {
	private static ClientCommunicationManager instance;
	
	public static ClientCommunicationManager getInstance() {

		if (instance == null) {
			instance = new ClientCommunicationManager();
			instance.messageSender = new ClientMessageSender();
			instance.messageReceiver = new ClientMessageReceiver();
		}
		return instance;
	}
	
	private ClientCommunicationManager(){
	}
}
