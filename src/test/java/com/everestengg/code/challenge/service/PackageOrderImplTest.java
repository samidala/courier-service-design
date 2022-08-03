package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.bo.InputPackage;
import com.everestengg.code.challenge.bo.Package;
import com.everestengg.code.challenge.model.PackageChargeInformation;
import com.everestengg.code.challenge.util.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PackageOrderImplTest {

    @BeforeAll
    static void beforeAll(){
        Utils.prepareOffers();
    }
    @Test
    void testCalcDiscountOfr001(){
        PackageOrderImpl packageOrderImpl = new PackageOrderImpl();
        Package pkg = Package.builder().packageId("P1").weight((short) 5).dist((short)5).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFR001");
        PackageChargeInformation packageChargeInformation = packageOrderImpl.calcCost(inputPackage,100);
        packageChargeInformation.print();
        Assertions.assertEquals("P1", packageChargeInformation.getPackageId());
        Assertions.assertEquals(0, packageChargeInformation.getTotalDiscount());
        Assertions.assertEquals(175, packageChargeInformation.getCostAfterDiscount());
    }
    @Test
    void testCalcDiscountOfr002(){
        PackageOrderImpl packageOrderImpl = new PackageOrderImpl();
        Package pkg = Package.builder().packageId("P2").weight((short) 15).dist((short)5).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFR002");
        PackageChargeInformation packageChargeInformation = packageOrderImpl.calcCost(inputPackage,100);
        packageChargeInformation.print();
        Assertions.assertEquals("P2", packageChargeInformation.getPackageId());
        Assertions.assertEquals(0, packageChargeInformation.getTotalDiscount());
        Assertions.assertEquals(275, packageChargeInformation.getCostAfterDiscount());
    }

    @Test
    void testCalcDiscountOfr003(){
        PackageOrderImpl packageOrderImpl = new PackageOrderImpl();
        Package pkg = Package.builder().packageId("P3").weight((short) 10).dist((short)100).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFR003");
        PackageChargeInformation packageChargeInformation = packageOrderImpl.calcCost(inputPackage,100);
        packageChargeInformation.print();
        Assertions.assertEquals("P3", packageChargeInformation.getPackageId());
        Assertions.assertEquals(35, packageChargeInformation.getTotalDiscount());
        Assertions.assertEquals(665, packageChargeInformation.getCostAfterDiscount());
    }

    @Test
    void testCalcDiscountInvalidOfferCode(){
        PackageOrderImpl packageOrderImpl = new PackageOrderImpl();
        Package pkg = Package.builder().packageId("P3").weight((short) 10).dist((short)100).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFROO003");
        PackageChargeInformation packageChargeInformation = packageOrderImpl.calcCost(inputPackage,100);
        packageChargeInformation.print();
        Assertions.assertEquals("P3", packageChargeInformation.getPackageId());
        Assertions.assertEquals(0, packageChargeInformation.getTotalDiscount());
        Assertions.assertEquals(700, packageChargeInformation.getCostAfterDiscount());
    }

    @Test
    void testCalcDiscountCategoryAndWeight(){
        //Not a perfect implementation however have done this for showcasing the design can be extendible to new offer
        // codes on different criteria like category.
        PackageOrderImpl packageOrderImpl = new PackageOrderImpl();
        Package pkg = Package.builder().packageId("electronics").weight((short) 80).dist((short)100).build();
        InputPackage inputPackage = new InputPackage(pkg,"OFR004");
        PackageChargeInformation packageChargeInformation = packageOrderImpl.calcCost(inputPackage,100);
        packageChargeInformation.print();
        Assertions.assertEquals("electronics", packageChargeInformation.getPackageId());
        Assertions.assertEquals(700, packageChargeInformation.getTotalDiscount());
        Assertions.assertEquals(700, packageChargeInformation.getCostAfterDiscount());
    }


}
