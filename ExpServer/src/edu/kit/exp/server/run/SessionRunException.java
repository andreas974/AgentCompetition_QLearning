package edu.kit.exp.server.run;

public class SessionRunException extends Exception {

	private static final long serialVersionUID = 507339651445598747L;
	private String message;
	
	public SessionRunException(String msg) {

		this.message = "Error while running session! Cause: " + msg;

	}

	@Override
	public String getMessage() {

		return this.message;
	}

}
