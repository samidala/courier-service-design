package com.everestengg.code.challenge.service.delivery.cost.estimation;

import com.everestengg.code.challenge.model.PackageDeliveryCostEstimateInfo;
import com.everestengg.code.challenge.service.offer.OfferServiceImpl;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Response;

/**
 * implementation of @{@link PackageDeliveryCostEstimationService}
 */
public class PackageDeliveryCostEstimationImpl implements PackageDeliveryCostEstimationService {

    public static final int WEIGHT_MULTIPLIER = 10;
    public static final int DISTANCE_MULTIPLIER = 5;

    private final int weightMultiplier;
    private final int distanceMultiplier;

    public PackageDeliveryCostEstimationImpl(){
        this.weightMultiplier = WEIGHT_MULTIPLIER;
        this.distanceMultiplier = DISTANCE_MULTIPLIER;
    }

    public PackageDeliveryCostEstimationImpl(int weightMultiplier, int distanceMultiplier) {
        this.weightMultiplier = weightMultiplier;
        this.distanceMultiplier = distanceMultiplier;
    }

    /**
     *
     * @param inputPackage packages to be delivered
     * @param baseDeliveryCost base delivery cost
     * @return PackageChargeInformation
     */
    public Response<PackageDeliveryCostEstimateInfo> calcCost(InputPackage inputPackage, float baseDeliveryCost){
        float discountValueInPercentage = new OfferServiceImpl().calcDiscount(inputPackage)/100;
        float totalCost = baseDeliveryCost + (inputPackage.getPackageDetails().getWeight() * getWeightMultiplier())
                + (inputPackage.getPackageDetails().getDist() * getDistanceMultiplier()) ;
        float totalDiscount = (totalCost *discountValueInPercentage);
        float costAfterDiscount = totalCost - totalDiscount;
        return Response.<PackageDeliveryCostEstimateInfo>builder()
                .errorCode(null)
                .result(PackageDeliveryCostEstimateInfo.builder().packageId(inputPackage
                                .getPackageDetails().getPackageId())
                        .totalCost(totalCost)
                        .totalDiscount(totalDiscount).costAfterDiscount(costAfterDiscount).build())
                .build();
    }

    public int getDistanceMultiplier() {
        return distanceMultiplier;
    }

    public int getWeightMultiplier() {
        return weightMultiplier;
    }
}
