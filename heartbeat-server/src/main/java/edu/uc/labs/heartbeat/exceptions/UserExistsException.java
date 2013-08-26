package edu.uc.labs.heartbeat.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String msg){
        super(msg);
    }
}
