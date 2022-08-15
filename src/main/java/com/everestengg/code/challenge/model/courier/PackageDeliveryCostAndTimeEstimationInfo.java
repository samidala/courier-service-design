package com.everestengg.code.challenge.model.courier;

import lombok.Builder;
import lombok.Getter;

/**
 * captures information of package, charges, discounts and estimated delivery time
 */
@Getter
@Builder(toBuilder = true)
public class PackageDeliveryCostAndTimeEstimationInfo {
    private PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo;

    private String packageId;
    private double estimatedDeliveryTime;

    public String toString() {
        return (packageDeliveryCostEstimateInfo != null ? packageDeliveryCostEstimateInfo : "")  +" "+estimatedDeliveryTime;
    }

}