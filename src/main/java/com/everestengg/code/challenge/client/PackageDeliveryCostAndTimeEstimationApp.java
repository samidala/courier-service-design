package com.everestengg.code.challenge.client;

import com.everestengg.code.challenge.io.IoUtils;
import com.everestengg.code.challenge.model.courier.PackageDeliveryCostAndTimeEstimationInfo;
import com.everestengg.code.challenge.service.delivery.helper.PackageDeliveryCostAndTimeEstimationServiceHelper;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.PackageDeliveryInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

import static com.everestengg.code.challenge.offer.repo.StaticOfferRepository.prepareOffers;


public class PackageDeliveryCostAndTimeEstimationApp {
    private static final Logger logger = LoggerFactory.getLogger(PackageDeliveryCostAndTimeEstimationApp.class);
    public static void main(String[] args) {
        prepareOffers();
        try(Scanner scanner = new Scanner(System.in)) {
            IoUtils.readCsv(scanner);
            /**
             * Read base delivery cost and no of packages
             */
            System.out.println("enter base delivery cost and no of packages");
            short baseDeliveryCost = IoUtils.readBaseCost(scanner);
            short noOfPackages = IoUtils.readNoOfPackages(scanner);;

            InputPackage[] inputPackages = new InputPackage[noOfPackages];
            // Read packages
            readPackages(scanner, noOfPackages, inputPackages);

            // read delivery information
            PackageDeliveryInput packageDeliveryInput = readDeliveryInput(scanner);
            //calculate discount for each and print
            List<PackageDeliveryCostAndTimeEstimationInfo> result = new PackageDeliveryCostAndTimeEstimationServiceHelper()
                    .calculateEstimatedDelivery(inputPackages, packageDeliveryInput, baseDeliveryCost);
            printDelivery(result);
        }catch (Exception e){
            logger.error("app crashed.. error ",e);
        }

    }

    private static void readPackages(Scanner scanner, short noOfPackages, InputPackage[] inputPackages) {
        IoUtils.readPackages(scanner, noOfPackages, inputPackages);
    }
    private static void printDelivery(List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfo) {
        for (PackageDeliveryCostAndTimeEstimationInfo packageDeliveryCostAndTimeEstimationInfo1 : packageDeliveryCostAndTimeEstimationInfo) {
            System.out.println(packageDeliveryCostAndTimeEstimationInfo1);
        }
    }


    private static PackageDeliveryInput readDeliveryInput(Scanner scanner) {
        System.out.println("enter no of vehicles, max speed and max carriable weight");
        PackageDeliveryInput pkgDeliveryInput =  PackageDeliveryInput.builder().noOfVehicle(scanner.nextShort())
                .maxSpeed(scanner.nextShort())
                .maxCarriableWt(scanner.nextShort()).build();
        if(!pkgDeliveryInput.isNoOfVehicleValid()){
            System.err.println("invalid number of vehicles, exiting app");
            System.exit(0);
        }
        if(!pkgDeliveryInput.isValidMaxSpeed()){
            System.err.println("invalid max speed, exiting app");
            System.exit(0);
        }
        if(!pkgDeliveryInput.isValidMaxCarriableWt()){
            System.err.println("invalid max carriable weight, exiting app");
            System.exit(0);
        }
        return pkgDeliveryInput;
    }
}

