package com.everestengg.code.challenge.vo;

import lombok.*;
import org.apache.commons.lang.StringUtils;

/**
 * captures package details
 */
@Builder
@Getter
@ToString
public class Package {
    private String packageId ;
    private short weight ;
    private short dist ;

    public boolean isValidPackageId() {
        return !StringUtils.isEmpty(this.packageId);
    }
    public boolean isValidWeight(){
        return weight > 0 && weight < 1000;
    }
    public boolean isValidDistance(){
        return dist > 0 && dist < 1000;
    }

}
