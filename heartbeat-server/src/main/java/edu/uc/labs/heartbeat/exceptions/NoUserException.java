package edu.uc.labs.heartbeat.exceptions;

public class NoUserException extends RuntimeException {
    public NoUserException(String msg){
        super(msg);
    }
}
