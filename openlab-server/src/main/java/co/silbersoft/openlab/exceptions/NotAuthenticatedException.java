package co.silbersoft.openlab.exceptions;

public class NotAuthenticatedException extends RuntimeException {

	private static final long serialVersionUID = -8366469963471177626L;

	public NotAuthenticatedException(String msg){
		super(msg);
	}
}
