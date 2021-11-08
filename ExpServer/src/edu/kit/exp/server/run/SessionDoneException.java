package edu.kit.exp.server.run;

public class SessionDoneException extends Exception {

	private static final long serialVersionUID = 8663429093529523397L;

	private String message;

	public SessionDoneException() {

		message = "<html><body>Session was successfully finished before! Do you want to run int again?" + "<br>YES: Reinitialize session and start again! <b>All existing data will be lost!</b>"
				+ "<br>NO: Session will not be run again!</html></body>";

	}

	@Override
	public String getMessage() {

		return this.message;
	}

}
