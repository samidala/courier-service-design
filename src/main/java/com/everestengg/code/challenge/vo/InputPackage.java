package com.everestengg.code.challenge.vo;

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
public class InputPackage {
    private Package packageDetails;
    private String offerCode;

    public boolean isValidOfferCode(){
        return !StringUtils.isEmpty(offerCode);
    }
}
