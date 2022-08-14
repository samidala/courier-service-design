package com.everestengg.code.challenge.exceptions;

public class InvalidValueException extends RuntimeException {
    private final String message;
    public InvalidValueException(String message, Exception e){
        super(e);
        this.message = message;
    }
    public InvalidValueException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}