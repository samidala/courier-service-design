package com.techdisqus.courier.service.offer;

import com.techdisqus.courier.vo.courier.CourierRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.techdisqus.courier.service.offer.OfferFactory.Type.IN_MEMORY;

/**
 * helper class to get the discount value
 */
public class OfferServiceImpl {
    private static Logger LOGGER = LoggerFactory.getLogger(OfferServiceImpl.class);
    public float getDiscountValue(CourierRequest courierRequest) {
        String offerCode = courierRequest.getOfferCode();
        float discount = OfferFactory.getOfferManager(IN_MEMORY).getOffer(offerCode).calcDiscount(
                PackageRequestContext.builder().courierRequest(courierRequest).build());
        LOGGER.debug("discount {}",discount);
        return discount;
    }


}
