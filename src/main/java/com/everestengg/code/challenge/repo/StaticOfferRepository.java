package com.everestengg.code.challenge.repo;

import com.everestengg.code.challenge.model.NumberOfferCriteria;
import com.everestengg.code.challenge.model.NumberRangeOfferCriteria;
import com.everestengg.code.challenge.model.Offer;
import com.everestengg.code.challenge.model.OfferCriteria;
import com.everestengg.code.challenge.model.StringOfferCriteria;
import com.everestengg.code.challenge.vo.Package;

import static com.everestengg.code.challenge.model.OfferCriteria.Operator.EQ;
import static com.everestengg.code.challenge.model.OfferCriteria.Operator.LT;
import static com.everestengg.code.challenge.model.OfferCriteria.Operator.RANGE;

public class StaticOfferRepository {

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
