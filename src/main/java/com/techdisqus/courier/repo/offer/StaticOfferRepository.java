package com.techdisqus.courier.repo.offer;

import com.techdisqus.courier.domain.offer.Offer;
import com.techdisqus.courier.domain.offer.OfferCriteria;

import java.util.Arrays;


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
        OfferCriteria dist50To250 = OfferCriteria.builder()
                .property("dist").valueHandler(OfferCriteria.distanceValueHandler).propertyValues(Arrays.asList("50","250")).build();
        OfferCriteria weight10To150 = OfferCriteria.builder()
                .property("weight").valueHandler(OfferCriteria.weightValueHandler).propertyValues(Arrays.asList("10","150")).build();
        new Offer("OFR003",5, dist50To250,weight10To150);
    }

    private static void prepareOffer2() {

        OfferCriteria dist50To150 = OfferCriteria.builder()
                .property("dist").valueHandler(OfferCriteria.distanceValueHandler).propertyValues(Arrays.asList("50","150")).build();
        OfferCriteria weight100To250 = OfferCriteria.builder().valueHandler(OfferCriteria.weightValueHandler)
                .property("weight").propertyValues(Arrays.asList("100","250")).build();
        new Offer("OFR002",7, dist50To150,weight100To250);
    }

    private static void prepareOffer1() {
        OfferCriteria distOfferCriteria = OfferCriteria.builder()
                                        .property("dist").propertyValues(Arrays.asList("0","200"))
                                        .valueHandler(OfferCriteria.distanceValueHandler)
                                        .build();
        OfferCriteria weightOfferCriteria = OfferCriteria.builder().valueHandler(OfferCriteria.weightValueHandler)
                .property("weight").propertyValues(Arrays.asList("70","200")).build();
        new Offer("OFR001",10, distOfferCriteria,weightOfferCriteria);
    }

    private static void prepareOffer4() {
        OfferCriteria catOfferCriteria =  OfferCriteria.builder().valueHandler(OfferCriteria.categoryValueHandler)
                .propertyValues(Arrays.asList("electronics")).property("category").build();

        OfferCriteria weightOfferCriteria =  OfferCriteria.builder().valueHandler(OfferCriteria.weightValueHandler)
                .propertyValues(Arrays.asList("70","200")).property("weight").build();

        OfferCriteria[] offerCriterias = new OfferCriteria[]{catOfferCriteria,weightOfferCriteria};
        new Offer("OFR004",50,offerCriterias);
    }
}
