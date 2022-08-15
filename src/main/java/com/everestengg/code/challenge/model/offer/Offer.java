package com.everestengg.code.challenge.model.offer;

import com.everestengg.code.challenge.exceptions.InvalidValueException;
import com.everestengg.code.challenge.vo.Package;
import com.everestengg.code.challenge.service.offer.InmemoryOfferManager;
import com.everestengg.code.challenge.service.offer.PackageRequestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

@Getter
@ToString
public  class Offer {

    private final String offerId;
    private final OfferCriteria[] offerCriterias;
    private final float percentage;

    public static Offer NA = new Offer("NA",0);


    public Offer(String offerId,int percentage,OfferCriteria...  offerCriterias){
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
        for(OfferCriteria offerCriteria : offerCriterias){
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
     * @return property value
     */
    public  String getValue(Package aPackage, OfferCriteria offerCriteria) throws InvalidValueException{
        String value = "";
        try {
            value =  new ObjectMapper().convertValue(aPackage,Map.class).get(offerCriteria.getProperty()).toString();
            System.out.println("value "+value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if(StringUtils.isEmpty(value)){
            throw new InvalidValueException("invalid value");
        }
        return value;
    }

}
