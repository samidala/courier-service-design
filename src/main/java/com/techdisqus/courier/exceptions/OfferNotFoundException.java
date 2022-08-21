package com.techdisqus.courier.exceptions;

public class OfferNotFoundException extends RuntimeException {
    private final String offerCode;
    public OfferNotFoundException(String offerCode){
        super(offerCode);
        this.offerCode = offerCode;
    }
}