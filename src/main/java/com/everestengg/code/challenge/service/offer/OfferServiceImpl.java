package com.everestengg.code.challenge.service.offer;

import com.everestengg.code.challenge.vo.InputPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.everestengg.code.challenge.service.offer.OfferFactory.Type.IN_MEMORY;

/**
 * helper class to get the discount value
 */
public class OfferServiceImpl {
    private static Logger LOGGER = LoggerFactory.getLogger(OfferServiceImpl.class);
    public float getDiscountValue(InputPackage inputPackage) {
        String offerCode = inputPackage.getOfferCode();
        float discount = OfferFactory.getOfferManager(IN_MEMORY).getOffer(offerCode).calcDiscount(
                PackageRequestContext.builder().inputPackage(inputPackage).build());
        LOGGER.debug("discount {}",discount);
        return discount;
    }


}
