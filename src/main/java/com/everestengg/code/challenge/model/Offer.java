package com.everestengg.code.challenge.model;

import com.everestengg.code.challenge.vo.Package;
import com.everestengg.code.challenge.service.offer.InmemoryOfferManager;
import com.everestengg.code.challenge.service.offer.PackageRequestContext;
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

    /**
     * calculates the discount percentage and returns percentage configured
     * if no @{@link OfferCriteria} is configured the package is eligible for configured discount
     * if @{@link OfferCriteria} is configured and all must match to get the discount
     * @param context @{{@link PackageRequestContext}} holder for package details
     * @return discountValue configured
     */

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

    /**
     *
     * @param aPackage package in context
     * @param offerCriteria @{@link OfferCriteria}
     * @return package property value depending @{@link ValueHandler} implementation
     */
    public  InputValue getValue(Package aPackage, OfferCriteria<ConfigValue,InputValue> offerCriteria){
        return offerCriteria.getValueHandler().getValue(aPackage);
    }

}
