package com.everestengg.code.challenge.vo;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
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
        return weight > 0 ;
    }
    public boolean isValidDistance(){
        return dist > 0;
    }

}
