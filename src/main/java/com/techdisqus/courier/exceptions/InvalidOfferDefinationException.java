package com.techdisqus.courier.exceptions;

public class InvalidOfferDefinationException extends RuntimeException {
    private final String message;
    public InvalidOfferDefinationException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}