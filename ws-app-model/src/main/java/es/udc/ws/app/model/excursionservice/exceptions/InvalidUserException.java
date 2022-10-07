package es.udc.ws.app.model.excursionservice.exceptions;

public class InvalidUserException extends Exception{
    public InvalidUserException(String message){
        super(message);
    }
}
