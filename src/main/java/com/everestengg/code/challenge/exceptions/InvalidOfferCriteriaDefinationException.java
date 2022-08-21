package com.everestengg.code.challenge.exceptions;

public class InvalidOfferCriteriaDefinationException extends RuntimeException {
    private final String message;
    public InvalidOfferCriteriaDefinationException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}