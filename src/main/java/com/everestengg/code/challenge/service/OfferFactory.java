package com.everestengg.code.challenge.service;

/**
 * Factory class for {@link OfferManager} implementations
 */
public class OfferFactory {
    public enum Type{
        IN_MEMORY
    }
    private OfferFactory(){}
    public static OfferManager getOfferManager(Type type){
        OfferManager offerManager ;
        switch (type){
            case IN_MEMORY:
                offerManager = InmemoryOfferManager.getInstance();
                break;
            default: throw  new IllegalArgumentException("invalid type "+type);
        }
        return offerManager;
    }

}
