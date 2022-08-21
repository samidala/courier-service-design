package com.everestengg.code.challenge.vo.courier;

import lombok.Builder;
import lombok.Getter;

/**
 * captures information of package, charges, discounts and estimated delivery time
 */
@Getter
@Builder(toBuilder = true)
public class CourierResponse {
    private PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo;

    private String packageId;
    private double estimatedDeliveryTime;

    public String toString() {
        return (packageDeliveryCostEstimateInfo != null ? packageDeliveryCostEstimateInfo : "")  +" "+estimatedDeliveryTime;
    }

}