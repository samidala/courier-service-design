package com.techdisqus.courier.util;

import com.techdisqus.courier.vo.courier.CourierRequest;

public class ValidationUtils {

    private ValidationUtils(){

    }
    public static boolean isValidPackage(CourierRequest courierRequest){
        isValidOfferCode(courierRequest);
        return isValidPackageId(courierRequest) && isValidDistance(courierRequest) && isValidWeight(courierRequest);
    }

    public static boolean isValidPackageId(CourierRequest courierRequest) {
        if(!courierRequest.getPackageDetails().isValidPackageId()){
            System.err.println("invalid package ID");
            return false;
        }
        return true;
    }
    public static boolean isValidDistance(CourierRequest courierRequest){
        if(!courierRequest.getPackageDetails().isValidDistance()){
            System.err.println("Invalid distance, the value should be greater than 1 and less than equal to "+Short.MAX_VALUE);
            return false;
        }
        return true;
    }
    public static boolean isValidWeight(CourierRequest courierRequest){
        if(!courierRequest.getPackageDetails().isValidWeight()){
            System.err.println("Invalid weight, the value should be greater than 1 and less than equal to "+Short.MAX_VALUE);
            return false;
        }
        return true;
    }

    public static void isValidOfferCode(CourierRequest courierRequest){
        if(!courierRequest.isValidOfferCode()){
            System.err.println("Invalid offer code, continuing with no discount");
        }
    }


}
