package com.everestengg.code.challenge.util;

import com.everestengg.code.challenge.bo.InputPackage;
import com.everestengg.code.challenge.bo.Package;
import com.everestengg.code.challenge.model.*;

import java.util.Scanner;

import static com.everestengg.code.challenge.model.OfferCriteria.Operator.*;

public class Utils {

    private Utils(){

    }
    public static void readPackages(Scanner scanner, short noOfPackages, InputPackage[] inputPackages) {
        System.out.println("enter packageId, weight, distance and offer code");
        for(int i = 1; i <= noOfPackages; i++){
            String packageId = scanner.next();
            short weight = scanner.nextShort();
            short dist = scanner.nextShort();
            String offerCode = scanner.next();
            inputPackages[i-1] = new InputPackage(Package.builder()
                    .packageId(packageId)
                    .weight(weight)
                    .dist(dist)
                    .build(),
                    offerCode);
            //System.out.println("\n");
        }
    }

    public static void prepareOffers() {
        //OFFR1
        prepareOffer1();
        //OFFR2
        prepareOffer2();
        //OFFR3
        prepareOffer3();
        prepareOffer4();
    }

    private static void prepareOffer3() {
        NumberRangeOfferCriteria dist50To250 = new NumberRangeOfferCriteria( RANGE,
                new Number[]{50, 250}, Package::getDist);
        NumberRangeOfferCriteria weight10To150 = new NumberRangeOfferCriteria(RANGE,
                new Number[]{10,150}, Package::getWeight);
        new Offer<Number,Number>("OFR003",5,new OfferCriteria[]{dist50To250,weight10To150});
    }

    private static void prepareOffer2() {
        NumberRangeOfferCriteria dist50To150 = new NumberRangeOfferCriteria(RANGE,
                new Number[]{50, 150}, Package::getDist);
        NumberRangeOfferCriteria weight100To250 = new NumberRangeOfferCriteria(RANGE,
                new Number[]{100,250}, Package::getWeight);
        new Offer<Number,Number>("OFR002",7,new OfferCriteria[]{dist50To150,weight100To250});
    }

    private static void prepareOffer1() {
        NumberOfferCriteria distOfferCriteria = new NumberOfferCriteria(LT, 200, Package::getDist);
        NumberRangeOfferCriteria weightOfferCriteria = new NumberRangeOfferCriteria(RANGE, new Number[] {70,200},
                Package::getWeight);
        OfferCriteria[] offerCriterias = new OfferCriteria[]{distOfferCriteria,weightOfferCriteria};
        new Offer<Number,Number>("OFR001",10,offerCriterias);
    }

    private static void prepareOffer4() {
        StringOfferCriteria catOfferCriteria = new StringOfferCriteria(EQ, "electronics", Package::getPackageId) ;
        NumberRangeOfferCriteria weightOfferCriteria = new NumberRangeOfferCriteria(RANGE, new Number[] {70,200},
                Package::getWeight);
        OfferCriteria[] offerCriterias = new OfferCriteria[]{catOfferCriteria,weightOfferCriteria};
        new Offer<String,String>("OFR004",50,offerCriterias);
    }
}
