package com.everestengg.code.challenge.model;

import com.everestengg.code.challenge.bo.Package;
import com.everestengg.code.challenge.service.InmemoryOfferManager;
import com.everestengg.code.challenge.service.PackageRequestContext;
import lombok.Getter;

@Getter
public  class Offer<ConfigValue, InputValue> {

    private final String offerId;
    private final OfferCriteria<ConfigValue, InputValue>[] offerCriterias;
    private final float percentage;

    public static Offer<Object,Object> NA = new Offer("NA",0);


    public Offer(String offerId,int percentage,OfferCriteria<ConfigValue, InputValue>...  offerCriterias){
        this.offerId = offerId;
        this.percentage = percentage;
        this.offerCriterias = offerCriterias;
        InmemoryOfferManager.getInstance().register(this);
    }

    public float calcDiscount(PackageRequestContext context){
        Package pkg = context.getInputPackage().getPackageDetails();
        if(offerCriterias == null || offerCriterias.length == 0){
            return percentage;
        }
        for(OfferCriteria<ConfigValue,InputValue> offerCriteria : offerCriterias){
            if(!offerCriteria.isMatch(getValue(pkg,offerCriteria))){
                return 0;
            }
        }
        return percentage;

    }
    public  InputValue getValue(Package aPackage, OfferCriteria<ConfigValue,InputValue> tNumberOfferCriteria){
        return tNumberOfferCriteria.getValueHandler().getValue(aPackage);
    }

}
