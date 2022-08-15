package com.everestengg.code.challenge.repo.offer;

import com.everestengg.code.challenge.domain.offer.Offer;
import com.everestengg.code.challenge.domain.offer.OfferCriteria;

import static com.everestengg.code.challenge.domain.offer.OfferCriteria.Operator.LT;
import static com.everestengg.code.challenge.domain.offer.OfferCriteria.Operator.RANGE;

public class StaticOfferRepository {

    public static void prepareOffers() {
        //OFFR1
        prepareOffer1();
        //OFFR2
        prepareOffer2();
        //OFFR3
        prepareOffer3();
    }

    private static void prepareOffer3() {
        OfferCriteria dist50To250 = OfferCriteria.builder().operator(RANGE)
                .property("dist").propertyValue("50|250").build();
        OfferCriteria weight10To150 = OfferCriteria.builder().operator(RANGE)
                .property("weight").propertyValue("10|150").build();
        new Offer("OFR003",5, dist50To250,weight10To150);
    }

    private static void prepareOffer2() {

        OfferCriteria dist50To150 = OfferCriteria.builder().operator(RANGE)
                .property("dist").propertyValue("50|150").build();
        OfferCriteria weight100To250 = OfferCriteria.builder().operator(RANGE)
                .property("weight").propertyValue("100|250").build();
        new Offer("OFR002",7, dist50To150,weight100To250);
    }

    private static void prepareOffer1() {
        OfferCriteria distOfferCriteria = OfferCriteria.builder().operator(LT)
                                        .property("dist").propertyValue("200").build();
        OfferCriteria weightOfferCriteria = OfferCriteria.builder().operator(RANGE)
                .property("weight").propertyValue("70|200").build();
        new Offer("OFR001",10, distOfferCriteria,weightOfferCriteria);
    }
}
