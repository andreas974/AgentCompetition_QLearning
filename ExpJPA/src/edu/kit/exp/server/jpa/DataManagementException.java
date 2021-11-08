package edu.kit.exp.server.jpa;

public class DataManagementException extends Exception {

	private static final long serialVersionUID = 8726008169163308844L;
	private String message;

	public DataManagementException(String msg) {

		this.message = msg;

	}

	@Override
	public String getMessage() {

		return this.message;
	}
}
