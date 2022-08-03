package com.everestengg.code.challenge.model;

import lombok.Builder;
import lombok.Getter;

/**
 * captures information of package, charges, discounts and estimated delivery time
 */
@Getter
@Builder
public class DeliveryOrder{
    private PackageChargeInformation packageChargeInformation;
    private float estimatedDeliveryTime;
    public String toString() {
        return packageChargeInformation.toString()+" "+estimatedDeliveryTime;
    }

}