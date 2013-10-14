package edu.uc.labs.heartbeat.exceptions;

public class CommandFailedException extends RuntimeException {
    
    public CommandFailedException(String msg){
        super(msg);
    }
    
}
