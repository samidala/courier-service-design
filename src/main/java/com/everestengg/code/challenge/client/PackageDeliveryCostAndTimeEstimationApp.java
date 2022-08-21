package com.everestengg.code.challenge.client;

import com.everestengg.code.challenge.io.IoUtils;
import com.everestengg.code.challenge.vo.courier.CourierResponse;
import com.everestengg.code.challenge.service.delivery.helper.PackageDeliveryCostAndTimeEstimationServiceHelper;
import com.everestengg.code.challenge.vo.courier.CourierRequest;
import com.everestengg.code.challenge.vo.VehicleInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

import static com.everestengg.code.challenge.repo.offer.StaticOfferRepository.prepareOffers;


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

            CourierRequest[] courierRequests = new CourierRequest[noOfPackages];
            // Read packages
            readPackages(scanner, noOfPackages, courierRequests);

            // read delivery information
            VehicleInformation vehicleInformation = readDeliveryInput(scanner);
            //calculate discount for each and print
            List<CourierResponse> result = new PackageDeliveryCostAndTimeEstimationServiceHelper()
                    .calculateEstimatedDelivery(courierRequests, vehicleInformation, baseDeliveryCost);
            printDelivery(result);
        }catch (Exception e){
            logger.error("app crashed.. error ",e);
        }

    }

    private static void readPackages(Scanner scanner, short noOfPackages, CourierRequest[] courierRequests) {
        IoUtils.readPackages(scanner, noOfPackages, courierRequests);
    }
    private static void printDelivery(List<CourierResponse> courierResponse) {
        for (CourierResponse courierResponse1 : courierResponse) {
            System.out.println(courierResponse1);
        }
    }


    private static VehicleInformation readDeliveryInput(Scanner scanner) {
        System.out.println("enter no of vehicles, max speed and max carriable weight");
        VehicleInformation pkgDeliveryInput =  VehicleInformation.builder().noOfVehicle(scanner.nextShort())
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

