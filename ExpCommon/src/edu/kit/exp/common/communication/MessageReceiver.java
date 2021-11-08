package edu.kit.exp.common.communication;

public abstract class MessageReceiver<T> {
	public abstract void routeMessage(T msg) throws Exception;
}
