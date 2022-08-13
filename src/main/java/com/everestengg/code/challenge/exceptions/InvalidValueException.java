package com.everestengg.code.challenge.exceptions;

public class InvalidValueException extends RuntimeException {
    private final String val;
    public InvalidValueException(String val,Exception e){
        super(e);
        this.val = val;
    }
}