package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.model.PackageDeliveryCostEstimateInfo;
import com.everestengg.code.challenge.repo.StaticOfferRepository;
import com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationImpl;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Package;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DeliveryCostEstimationImplTest {

    @BeforeAll
    static void beforeAll(){
        StaticOfferRepository.prepareOffers();
    }
    @Test
    void testCalcDiscountOfr001(){
        PackageDeliveryCostEstimationImpl packageOrderImpl = new PackageDeliveryCostEstimationImpl();
        Package pkg = Package.builder().packageId("P1").weight((short) 5).dist((short)5).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFR001");
        PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = packageOrderImpl
                .calcCost(inputPackage,100).getResult();
        packageDeliveryCostEstimateInfo.print();
        Assertions.assertEquals("P1", packageDeliveryCostEstimateInfo.getPackageId());
        Assertions.assertEquals(0, packageDeliveryCostEstimateInfo.getTotalDiscount());
        Assertions.assertEquals(175, packageDeliveryCostEstimateInfo.getCostAfterDiscount());
    }
    @Test
    void testCalcDiscountOfr002(){
        PackageDeliveryCostEstimationImpl packageOrderImpl = new PackageDeliveryCostEstimationImpl();
        Package pkg = Package.builder().packageId("P2").weight((short) 15).dist((short)5).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFR002");
        PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = packageOrderImpl
                .calcCost(inputPackage,100).getResult();
        packageDeliveryCostEstimateInfo.print();
        Assertions.assertEquals("P2", packageDeliveryCostEstimateInfo.getPackageId());
        Assertions.assertEquals(0, packageDeliveryCostEstimateInfo.getTotalDiscount());
        Assertions.assertEquals(275, packageDeliveryCostEstimateInfo.getCostAfterDiscount());
    }

    @Test
    void testCalcDiscountOfr003(){
        PackageDeliveryCostEstimationImpl packageOrderImpl = new PackageDeliveryCostEstimationImpl();
        Package pkg = Package.builder().packageId("P3").weight((short) 10).dist((short)100).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFR003");
        PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = packageOrderImpl
                .calcCost(inputPackage,100).getResult();
        packageDeliveryCostEstimateInfo.print();
        Assertions.assertEquals("P3", packageDeliveryCostEstimateInfo.getPackageId());
        Assertions.assertEquals(35, packageDeliveryCostEstimateInfo.getTotalDiscount());
        Assertions.assertEquals(665, packageDeliveryCostEstimateInfo.getCostAfterDiscount());
    }

    @Test
    void testCalcDiscountInvalidOfferCode(){
        PackageDeliveryCostEstimationImpl packageOrderImpl = new PackageDeliveryCostEstimationImpl();
        Package pkg = Package.builder().packageId("P3").weight((short) 10).dist((short)100).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFROO003");
        PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = packageOrderImpl
                .calcCost(inputPackage,100).getResult();
        packageDeliveryCostEstimateInfo.print();
        Assertions.assertEquals("P3", packageDeliveryCostEstimateInfo.getPackageId());
        Assertions.assertEquals(0, packageDeliveryCostEstimateInfo.getTotalDiscount());
        Assertions.assertEquals(700, packageDeliveryCostEstimateInfo.getCostAfterDiscount());
    }

    @Test
    void testCalcDiscountCategoryAndWeight(){
        //Not a perfect implementation however have done this for showcasing the design can be extendible to new offer
        // codes on different criteria like category.
        PackageDeliveryCostEstimationImpl packageOrderImpl = new PackageDeliveryCostEstimationImpl();
        Package pkg = Package.builder().packageId("electronics").weight((short) 80).dist((short)100).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFR004");
        PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = packageOrderImpl
                .calcCost(inputPackage,100).getResult();
        packageDeliveryCostEstimateInfo.print();
        Assertions.assertEquals("electronics", packageDeliveryCostEstimateInfo.getPackageId());
        Assertions.assertEquals(700, packageDeliveryCostEstimateInfo.getTotalDiscount());
        Assertions.assertEquals(700, packageDeliveryCostEstimateInfo.getCostAfterDiscount());
    }


}
