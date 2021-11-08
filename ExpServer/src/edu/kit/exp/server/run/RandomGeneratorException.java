package edu.kit.exp.server.run;

/**
 * Exception for errors during the number generation process.
 *
 */
public class RandomGeneratorException extends Exception {


	private static final long serialVersionUID = -2413739602716260398L;
	private String message;

	
	public RandomGeneratorException(String msg) {

		this.message = "Can not create random numbers! Cause: " + msg;

	}

	@Override
	public String getMessage() {

		return this.message;
	}
}
