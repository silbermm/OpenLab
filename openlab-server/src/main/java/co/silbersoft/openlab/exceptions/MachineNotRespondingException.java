package co.silbersoft.openlab.exceptions;


public class MachineNotRespondingException extends RuntimeException {
    public MachineNotRespondingException(String message){
        super(message);
    }
}
