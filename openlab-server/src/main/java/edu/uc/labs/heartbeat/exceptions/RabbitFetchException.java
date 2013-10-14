package edu.uc.labs.heartbeat.exceptions;

public class RabbitFetchException extends RuntimeException{
    public RabbitFetchException(String message){
        super(message);
    }
}
