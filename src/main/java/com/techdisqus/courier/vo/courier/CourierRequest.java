package com.techdisqus.courier.vo.courier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

/**
 * captures package to be delivered and offer code if any
 */
@Getter
@AllArgsConstructor
@ToString
public class CourierRequest {
    private Package packageDetails;
    private String offerCode;

    public boolean isValidOfferCode(){
        return !StringUtils.isEmpty(offerCode);
    }
}
