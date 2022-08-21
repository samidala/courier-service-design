package com.techdisqus.courier.client;

import com.techdisqus.courier.vo.courier.PackageDeliveryCostEstimateInfo;
import com.techdisqus.courier.repo.offer.StaticOfferRepository;
import com.techdisqus.courier.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory;
import com.techdisqus.courier.io.IoUtils;
import com.techdisqus.courier.vo.courier.CourierRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class PackageDeliveryCostEstimationApp {
    private static final Logger logger = LoggerFactory.getLogger(PackageDeliveryCostEstimationApp.class);
    public static void main(String[] args) {
        prepareOffers();
        try(Scanner scanner = new Scanner(System.in)) {
            IoUtils.readCsv(scanner);
            System.out.println("Enter base delivery cost and number of packages");
            short baseDeliveryCost = IoUtils.readBaseCost(scanner);
            short noOfPackages = IoUtils.readNoOfPackages(scanner);
            CourierRequest[] courierRequests = new CourierRequest[noOfPackages];
            // Read packages
            IoUtils.readPackages(scanner, noOfPackages, courierRequests);
            //calculate discount for each and print
            process(baseDeliveryCost, courierRequests);
        }catch (Exception e){
            logger.error("app crashed.. error ",e);
        }

    }





    private static void process(short baseDeliveryCost, CourierRequest[] courierRequests) {
        for (CourierRequest courierRequest : courierRequests){
            PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = PackageDeliveryCostEstimationServiceFactory
                    .getPackageDeliveryCostEstimationService(PackageDeliveryCostEstimationServiceFactory.PackageServiceType.SIMPLE).calcCost(courierRequest, baseDeliveryCost).getResult();
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
