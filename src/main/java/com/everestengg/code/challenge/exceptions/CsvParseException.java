package com.everestengg.code.challenge.exceptions;

public class CsvParseException extends RuntimeException {
    private final String message;
    public CsvParseException(String message, Exception e){
        super(e);
        this.message = message;
    }
    public CsvParseException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}