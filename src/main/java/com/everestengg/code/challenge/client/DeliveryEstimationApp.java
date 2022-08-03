package com.everestengg.code.challenge.client;

import com.everestengg.code.challenge.bo.DeliveryInput;
import com.everestengg.code.challenge.bo.InputPackage;
import com.everestengg.code.challenge.model.DeliveryOrder;
import com.everestengg.code.challenge.service.DeliveryEstimationServiceFactory;
import com.everestengg.code.challenge.util.Utils;

import java.util.*;


import static com.everestengg.code.challenge.service.DeliveryEstimationServiceFactory.DeliveryEstimationType.SIMPLE;
import static com.everestengg.code.challenge.util.Utils.prepareOffers;

public class DeliveryEstimationApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        prepareOffers();
        /**
         * Read base delivery cost and no of packages
         */
        System.out.println("enter base delivery cost and no of packages");
        short baseDeliveryCost = scanner.nextShort();
        short noOfPackages = scanner.nextShort();
        InputPackage[] inputPackages = new InputPackage[noOfPackages];
        // Read packages
        readPackages(scanner, noOfPackages, inputPackages);

        // read delivery information
        DeliveryInput deliveryInput= readDeliveryInput(scanner);
        //calculate discount for each and print
        List<DeliveryOrder> result = DeliveryEstimationServiceFactory
                .getDeliveryEstimationService(SIMPLE).calculateEstimatedDelivery(inputPackages,deliveryInput,
                        baseDeliveryCost);
        printDelivery(result);
        scanner.close();
    }

    private static void readPackages(Scanner scanner, short noOfPackages, InputPackage[] inputPackages) {
        Utils.readPackages(scanner, noOfPackages, inputPackages);
    }
    private static void printDelivery(List<DeliveryOrder> deliveryOrder) {
        for (DeliveryOrder deliveryOrder1 : deliveryOrder) {
            System.out.println(deliveryOrder1);
        }
    }


    private static DeliveryInput readDeliveryInput(Scanner scanner) {
        System.out.println("enter no of vehicles, max speed and max carriable weight");
        return DeliveryInput.builder().noOfVehicle(scanner.nextShort()).maxSpeed(scanner.nextShort())
                .maxCarriableWt(scanner.nextShort()).build();
    }
}

