package edu.uc.labs.heartbeat.models;

public class Failure {

    private String message;    

    public Failure(String message) {
        this.message = message;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }    
}
