package edu.kit.exp.server.structure;

public class StructureManagementException extends Exception {

	private static final long serialVersionUID = 7245168988709666841L;
	
	private String message;

	public StructureManagementException(String msg) {
		
		this.message = msg;		
		
	}

	@Override
	public String getMessage() {
		
		return this.message;
	}

}
