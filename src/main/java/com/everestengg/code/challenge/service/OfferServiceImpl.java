package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.bo.InputPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.everestengg.code.challenge.service.OfferFactory.Type.IN_MEMORY;

public class OfferServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(OfferServiceImpl.class);
    public float calcDiscount(InputPackage inputPackage) {
        String offerCode = inputPackage.getOfferCode();
        float discount = OfferFactory.getOfferManager(IN_MEMORY).getOffer(offerCode).calcDiscount(
                PackageRequestContext.builder().inputPackage(inputPackage).build());
        logger.debug("discount {}",discount);
        return discount;
    }


}
