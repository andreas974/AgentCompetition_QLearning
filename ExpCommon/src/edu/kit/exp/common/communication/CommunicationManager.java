package edu.kit.exp.common.communication;

public abstract class CommunicationManager<T, U extends MessageReceiver<T>, V extends MessageSender> {
	
	protected IncommingMessageQueue<T> incommingMessageQueue;
	protected U messageReceiver;
	protected V messageSender;
	
	public CommunicationManager(){
		incommingMessageQueue = new IncommingMessageQueue<T>();
	}

	public IncommingMessageQueue<T> getIncommingMessageQueue() {
		return incommingMessageQueue;
	}

	public U getMessageReceiver() {
		return messageReceiver;
	}
	
	public V getMessageSender() {
		return messageSender;
	}
}
