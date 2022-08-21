package com.techdisqus.courier.service.offer;

import com.techdisqus.courier.exceptions.OfferNotFoundException;
import com.techdisqus.courier.domain.offer.Offer;

/**
 * OfferManager is specification for offer registration and getting the offer details by offer code
 */
public interface OfferManager {

    void register(Offer offer);
     Offer getOffer(String offerCode) throws OfferNotFoundException;


}
