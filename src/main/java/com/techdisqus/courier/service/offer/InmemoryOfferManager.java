package com.techdisqus.courier.service.offer;

import com.techdisqus.courier.domain.offer.Offer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * in-memory implementation of @{@link OfferManager}
 */
public final class InmemoryOfferManager implements OfferManager, Serializable,Cloneable {

    private static final InmemoryOfferManager INSTANCE = new InmemoryOfferManager();
    private static final Map<String, Offer> offerCache = new HashMap<>();

    private InmemoryOfferManager(){

    }
    public  void register(Offer offer){
        offerCache.put(offer.getOfferId(),offer);
    }
    public  Offer getOffer(String offerCode) throws IllegalArgumentException{
        return offerCache.getOrDefault(offerCode,Offer.NA);

    }

    public static InmemoryOfferManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw  new CloneNotSupportedException("not supported");
    }
    protected Object readResolve() {
        return INSTANCE;
    }
}
