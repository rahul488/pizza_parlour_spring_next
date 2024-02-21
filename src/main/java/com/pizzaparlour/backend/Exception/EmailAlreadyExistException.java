package com.pizzaparlour.backend.Exception;

public class EmailAlreadyExistException extends RuntimeException{

    public EmailAlreadyExistException(String message){
        super(message);
    }
}
