package edu.uc.labs.heartbeat.domain;

public class CommandResult {

    private int exitCode;
    private String message;

    /**
     * @return the exitCode
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * @param exitCode the exitCode to set
     */
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }                
}
