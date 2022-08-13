package com.everestengg.code.challenge.service.offer;

import com.everestengg.code.challenge.exceptions.OfferNotFoundException;
import com.everestengg.code.challenge.model.Offer;

/**
 * OfferManager is specification for offer registration and getting the offer details by offer code
 */
public interface OfferManager {

    <ConfigValue, InputValue> void register(Offer<ConfigValue, InputValue> offer);
    <ConfigValue, InputValue> Offer<ConfigValue, InputValue> getOffer(String offerCode) throws OfferNotFoundException;


}
