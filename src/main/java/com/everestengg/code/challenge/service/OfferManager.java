package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.model.Offer;

public interface OfferManager {

    <ConfigValue, InputValue> void register(Offer<ConfigValue, InputValue> offer);
    <ConfigValue, InputValue> Offer<ConfigValue, InputValue> getOffer(String offerCode) throws OfferNotFoundException;

    class OfferNotFoundException extends RuntimeException {
    }
}
