package edu.kit.exp.server.gui;

/**
 * Exception for wrong data input.
 *
 */
public class DataInputException extends Exception {

	private static final long serialVersionUID = 7245168988709666841L;
	
	private String message;

	public DataInputException(String msg) {
		
		this.message = msg;		
		
	}

	@Override
	public String getMessage() {
		
		return this.message;
	}

}
