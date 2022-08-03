package com.everestengg.code.challenge.client;

import com.everestengg.code.challenge.model.PackageChargeInformation;
import com.everestengg.code.challenge.service.PackageOrderFactory;
import com.everestengg.code.challenge.util.Utils;
import com.everestengg.code.challenge.bo.InputPackage;

import java.util.Scanner;

import static com.everestengg.code.challenge.service.PackageOrderFactory.PackageServiceType.SIMPLE;

public class PackageChargeCalculatorApp {
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
        Utils.readPackages(scanner, noOfPackages, inputPackages);
        //calculate discount for each and print
        process(baseDeliveryCost, inputPackages);

    }

    private static void process(short baseDeliveryCost, InputPackage[] inputPackages) {
        for (InputPackage inputPackage : inputPackages){
            PackageChargeInformation packageChargeInformation = PackageOrderFactory.getPackageOrderService(SIMPLE).calcCost(inputPackage, baseDeliveryCost);
            packageChargeInformation.print();
        }
    }


    /**
     * Offer preparation below
     */
    private static void prepareOffers() {
      Utils.prepareOffers();
    }
}
