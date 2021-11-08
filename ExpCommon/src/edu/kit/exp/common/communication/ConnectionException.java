package edu.kit.exp.common.communication;

public class ConnectionException extends Exception {
	
	private static final long serialVersionUID = 3487613197235546928L;
	private String message;

	public ConnectionException(String msg) {
		this.message = "ExpClient could not connect to ExpServer. " + msg;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
