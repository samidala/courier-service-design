package com.everestengg.code.challenge.service.offer;

import com.everestengg.code.challenge.exceptions.OfferNotFoundException;
import com.everestengg.code.challenge.model.offer.Offer;

/**
 * OfferManager is specification for offer registration and getting the offer details by offer code
 */
public interface OfferManager {

    void register(Offer offer);
     Offer getOffer(String offerCode) throws OfferNotFoundException;


}
