package com.everestengg.code.challenge.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * captures package to be delivered and offer code if any
 */
@Getter
@AllArgsConstructor
@ToString
public class InputPackage {
    private Package packageDetails;
    private String offerCode;
}
