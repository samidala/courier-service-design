package com.everestengg.code.challenge.io;

import com.everestengg.code.challenge.util.ValidationUtils;
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
            ReadResult<Short> weightReadResult = getValue(scanner.next(),
                    "Enter valid value for weight between 1 to "+Short.MAX_VALUE);
            if(!weightReadResult.isValid()){
                System.out.println("re-enter packageId, weight, distance and offer code");
                continue;
            }
            ReadResult<Short> distReadResult = getValue(scanner.next(),
                    "Enter valid value for distance between 1 to "+Short.MAX_VALUE);
            if(!distReadResult.isValid()){
                System.out.println("re-enter packageId, weight, distance and offer code");
                continue;
            }
            String offerCode = scanner.next();
            InputPackage inputPackage = new InputPackage(Package.builder()
                    .packageId(packageId)
                    .weight(weightReadResult.getValue())
                    .dist(distReadResult.getValue())
                    .build(),
                    offerCode);
            if(ValidationUtils.isValidPackage(inputPackage)) {
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
    public static class ReadResult<T>{
        private final T value;
        private final boolean isValid;
    }
    public static ReadResult<Short> getValue(String strValue, String message){
        try{
            return ReadResult.<Short>builder().isValid(true).value(Short.parseShort(strValue)).build();
        }catch (Exception e){
            System.err.println(e.getMessage());
            System.err.println(message);

        }
        return ReadResult.<Short>builder().isValid(false).build();
    }

    public static Short readBaseCost(Scanner scanner){
        return readValue(scanner, "invalid base delivery cost, enter 1 to ");
    }

    private static Short readValue(Scanner scanner, String message) {
        ReadResult<Short> baseDeliveryCostReadResult =IoUtils.getValue( scanner.next(),
                message +Short.MAX_VALUE);
        if(!baseDeliveryCostReadResult.isValid()){
            System.err.println("exiting app");
            System.exit(0);
        }
        return baseDeliveryCostReadResult.getValue();
    }

    public static short readNoOfPackages(Scanner scanner){
        return readValue(scanner, "invalid number of packages, enter 1 to ");
    }


}
