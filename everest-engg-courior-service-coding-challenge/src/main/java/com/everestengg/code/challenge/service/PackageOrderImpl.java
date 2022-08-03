package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.bo.InputPackage;
import com.everestengg.code.challenge.model.PackageChargeInformation;

public class PackageOrderImpl implements PackageOrderService{

    public static final int WEIGHT_MULTIPLIER = 10;
    public static final int DISTANCE_MULTIPLIER = 5;

    private final int weightMultiplier;
    private final int distanceMultiplier;

    public PackageOrderImpl(){
        this.weightMultiplier = WEIGHT_MULTIPLIER;
        this.distanceMultiplier = DISTANCE_MULTIPLIER;
    }

    public PackageOrderImpl(int weightMultiplier, int distanceMultiplier) {
        this.weightMultiplier = weightMultiplier;
        this.distanceMultiplier = distanceMultiplier;
    }

    /**
     *
     * @param inputPackage packages to be delivered
     * @param baseDeliveryCost base delivery cost
     * @return PackageChargeInformation
     */
    public PackageChargeInformation calcCost(InputPackage inputPackage, float baseDeliveryCost){
        float discountValueInPercentage = new OfferServiceImpl().calcDiscount(inputPackage)/100;
        float totalCost = baseDeliveryCost + (inputPackage.getPackageDetails().getWeight() * getWeightMultiplier())
                + (inputPackage.getPackageDetails().getDist() * getDistanceMultiplier()) ;
        float totalDiscount = (totalCost *discountValueInPercentage);
        float costAfterDiscount = totalCost - totalDiscount;
        return PackageChargeInformation.builder().packageId(inputPackage.getPackageDetails().getPackageId())
                .totalCost(totalCost)
                .totalDiscount(totalDiscount).costAfterDiscount(costAfterDiscount).build();
    }

    public int getDistanceMultiplier() {
        return distanceMultiplier;
    }

    public int getWeightMultiplier() {
        return weightMultiplier;
    }
}
