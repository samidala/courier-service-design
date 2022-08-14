package com.everestengg.code.challenge.util;

import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Scanner;

public class ValidationUtils {

    private ValidationUtils(){

    }
    public static boolean isValidPackage(InputPackage inputPackage){
        isValidOfferCode(inputPackage);
        return isValidPackageId(inputPackage) && isValidDistance(inputPackage) && isValidWeight(inputPackage);
    }

    public static boolean isValidPackageId(InputPackage inputPackage) {
        if(!inputPackage.getPackageDetails().isValidPackageId()){
            System.err.println("invalid package ID");
            return false;
        }
        return true;
    }
    public static boolean isValidDistance(InputPackage inputPackage){
        if(!inputPackage.getPackageDetails().isValidDistance()){
            System.err.println("Invalid distance, the value should be greater than 1 and less than equal to "+Short.MAX_VALUE);
            return false;
        }
        return true;
    }
    public static boolean isValidWeight(InputPackage inputPackage){
        if(!inputPackage.getPackageDetails().isValidWeight()){
            System.err.println("Invalid weight, the value should be greater than 1 and less than equal to "+Short.MAX_VALUE);
            return false;
        }
        return true;
    }

    public static void isValidOfferCode(InputPackage inputPackage){
        if(!inputPackage.isValidOfferCode()){
            System.err.println("Invalid offer code, continuing with no discount");
        }
    }


}
