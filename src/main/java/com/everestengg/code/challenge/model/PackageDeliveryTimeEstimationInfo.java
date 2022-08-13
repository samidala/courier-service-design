package com.everestengg.code.challenge.model;

import lombok.Builder;
import lombok.Getter;

/**
 * captures information of package, charges, discounts and estimated delivery time
 */
@Getter
@Builder
public class PackageDeliveryTimeEstimationInfo {
    private PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo;
    private float estimatedDeliveryTime;
    public String toString() {
        return packageDeliveryCostEstimateInfo.toString()+" "+estimatedDeliveryTime;
    }

}