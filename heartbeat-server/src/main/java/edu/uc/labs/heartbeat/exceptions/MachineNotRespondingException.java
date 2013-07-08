package edu.uc.labs.heartbeat.exceptions;


public class MachineNotRespondingException extends RuntimeException {
    public MachineNotRespondingException(String message){
        super(message);
    }
}
