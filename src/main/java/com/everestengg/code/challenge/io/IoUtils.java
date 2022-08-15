package com.everestengg.code.challenge.io;

import com.everestengg.code.challenge.offer.repo.CsvOfferRepository;
import com.everestengg.code.challenge.util.ValidationUtils;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class IoUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoUtils.class);

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

    public static void readCsv(Scanner scanner){
        System.out.println("Read CSV file ? press Y and enter to read files else enter any key and enter");
        String readOffers = scanner.nextLine();
        if(!"Y".equals(readOffers)){
            LOGGER.info("input is Not Y and hence returning");
            return;
        }
        try {
            System.out.println("input csv file name for offer and offer criteria mapping inorder, space separated");
            String offerFileName = scanner.next();
            String offerCriteriaName = scanner.next();
            new CsvOfferRepository().prepareOffers(offerFileName,offerCriteriaName);
            System.out.println("offers loaded successfully");
        }catch (IOException e){
            LOGGER.error("error while reading csv",e);
            System.err.println("Error while reading files and exiting app. Please provide path, error "+e.getMessage());
            System.exit(0);
        }catch (Exception e){
            LOGGER.error("error while reading and processing csv",e);
            System.err.println("Error while reading files and processing.. exiting app");
            System.exit(0);
        }

    }

}
