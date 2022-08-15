package com.everestengg.code.challenge.client;

import com.everestengg.code.challenge.model.courier.PackageDeliveryCostEstimateInfo;
import com.everestengg.code.challenge.offer.repo.StaticOfferRepository;
import com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory;
import com.everestengg.code.challenge.io.IoUtils;
import com.everestengg.code.challenge.vo.InputPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory.PackageServiceType.SIMPLE;

public class PackageDeliveryCostEstimationApp {
    private static final Logger logger = LoggerFactory.getLogger(PackageDeliveryCostEstimationApp.class);
    public static void main(String[] args) {
        prepareOffers();
        try(Scanner scanner = new Scanner(System.in)) {
            IoUtils.readCsv(scanner);
            System.out.println("Enter base delivery cost and number of packages");
            short baseDeliveryCost = IoUtils.readBaseCost(scanner);
            short noOfPackages = IoUtils.readNoOfPackages(scanner);
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
                    .getPackageDeliveryCostEstimationService(SIMPLE).calcCost(inputPackage, baseDeliveryCost).getResult();
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
