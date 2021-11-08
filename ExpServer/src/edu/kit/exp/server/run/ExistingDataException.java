package edu.kit.exp.server.run;

public class ExistingDataException extends Exception {

	private static final long serialVersionUID = 1094560226687570266L;
	private String message;

	public ExistingDataException(String msg) {

		this.message = msg;

	}

	@Override
	public String getMessage() {

		return this.message;
	}
}
