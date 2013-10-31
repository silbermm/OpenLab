package co.silbersoft.openlab.exceptions;

public class EmptyPermissionException extends RuntimeException {

	private static final long serialVersionUID = 8403207846777471839L;

	public EmptyPermissionException(String msg){
		super(msg);
	}
	
}
