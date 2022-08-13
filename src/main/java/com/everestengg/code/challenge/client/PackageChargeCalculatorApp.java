package com.everestengg.code.challenge.client;

import com.everestengg.code.challenge.model.PackageDeliveryCostEstimateInfo;
import com.everestengg.code.challenge.repo.StaticOfferRepository;
import com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory;
import com.everestengg.code.challenge.util.IoUtils;
import com.everestengg.code.challenge.vo.InputPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory.PackageServiceType.SIMPLE;

public class PackageChargeCalculatorApp {
    private static final Logger logger = LoggerFactory.getLogger(PackageChargeCalculatorApp.class);
    public static void main(String[] args) {
        prepareOffers();
        try(Scanner scanner = new Scanner(System.in)) {
            /**
             * Read base delivery cost and no of packages
             */
            System.out.println("enter base delivery cost and no of packages");
            short baseDeliveryCost = scanner.nextShort();
            short noOfPackages = scanner.nextShort();
            InputPackage[] inputPackages = new InputPackage[noOfPackages];
            // Read packages
            IoUtils.readPackages(scanner, noOfPackages, inputPackages);
            //calculate discount for each and print
            process(baseDeliveryCost, inputPackages);
        }catch (Exception e){
            logger.error("app crashed.. error ",e);
        }

    }

    private static void process(short baseDeliveryCost, InputPackage[] inputPackages) {
        for (InputPackage inputPackage : inputPackages){
            PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = PackageDeliveryCostEstimationServiceFactory
                    .getPackageOrderService(SIMPLE).calcCost(inputPackage, baseDeliveryCost).getResult();
            packageDeliveryCostEstimateInfo.print();
        }
    }


    /**
     * Offer preparation below
     */
    private static void prepareOffers() {
      StaticOfferRepository.prepareOffers();
    }
}
