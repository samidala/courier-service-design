package com.everestengg.code.challenge.util;

import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Scanner;

public class IoUtils {

    private IoUtils(){

    }
    public static void readPackages(Scanner scanner, short noOfPackages, InputPackage[] inputPackages) {
        System.out.println("enter packageId, weight, distance and offer code");
        int i = 1;
        while (i <= noOfPackages){

            String packageId = scanner.next();
            ReadResult<Short> weightReadResult = getValue(scanner.next(), "Enter valid value for weight between 1 to "+Short.MAX_VALUE);
            if(!weightReadResult.isValid()){
                continue;
            }
            ReadResult<Short> distReadResult = getValue(scanner.next(), "Enter valid value for distance between 1 to "+Short.MAX_VALUE);
            if(!distReadResult.isValid()){
                continue;
            }
            String offerCode = scanner.next();
            InputPackage inputPackage = new InputPackage(Package.builder()
                    .packageId(packageId)
                    .weight(weightReadResult.getValue())
                    .dist(distReadResult.getValue())
                    .build(),
                    offerCode);
            if(isValidPackage(inputPackage)) {
                inputPackages[i - 1] = inputPackage;
                i++;
            }else{
                System.out.println("re-enter packageId, weight, distance and offer code");
            }
        }
    }


    @Builder
    @AllArgsConstructor
    @Getter
    private static class ReadResult<T>{
        private final T value;
        private final boolean isValid;
    }
    private static ReadResult<Short> getValue(String strValue, String message){
        try{
            return ReadResult.<Short>builder().isValid(true).value(Short.parseShort(strValue)).build();
        }catch (Exception e){
            System.out.println(message);
            System.out.println("re-enter packageId, weight, distance and offer code");
        }
        return ReadResult.<Short>builder().isValid(false).build();
    }
    private static boolean isValidPackage(InputPackage inputPackage){
        isValidOfferCode(inputPackage);
        return isValidPackageId(inputPackage) && isValidDistance(inputPackage) && isValidWeight(inputPackage);
    }

    private static boolean isValidPackageId(InputPackage inputPackage) {
        if(!inputPackage.getPackageDetails().isValidPackageId()){
            System.err.println("invalid package ID");
            return false;
        }
        return true;
    }
    private static boolean isValidDistance(InputPackage inputPackage){
        if(!inputPackage.getPackageDetails().isValidDistance()){
            System.err.println("Invalid distance, the value should be greater than 1 and less than equal to "+Short.MAX_VALUE);
            return false;
        }
        return true;
    }
    private static boolean isValidWeight(InputPackage inputPackage){
        if(!inputPackage.getPackageDetails().isValidWeight()){
            System.err.println("Invalid weight, the value should be greater than 1 and less than equal to "+Short.MAX_VALUE);
            return false;
        }
        return true;
    }

    private static void isValidOfferCode(InputPackage inputPackage){
        if(!inputPackage.isValidOfferCode()){
            System.err.println("Invalid offer code, continuing with no discount");
        }
    }


}
