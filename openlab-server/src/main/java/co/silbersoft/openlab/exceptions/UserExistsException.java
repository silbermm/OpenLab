package co.silbersoft.openlab.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String msg){
        super(msg);
    }
}
