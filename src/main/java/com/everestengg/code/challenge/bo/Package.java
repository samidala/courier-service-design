package com.everestengg.code.challenge.bo;

import lombok.*;


@Builder
@Getter
@ToString
public class Package {
    private String packageId ;
    private short weight ;
    private short dist ;
}
