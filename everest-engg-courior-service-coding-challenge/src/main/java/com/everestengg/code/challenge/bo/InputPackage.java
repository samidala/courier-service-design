package com.everestengg.code.challenge.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class InputPackage {
    private Package packageDetails;
    private String offerCode;
}
