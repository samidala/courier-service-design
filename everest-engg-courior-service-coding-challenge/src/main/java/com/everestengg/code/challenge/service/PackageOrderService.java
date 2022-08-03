package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.bo.InputPackage;
import com.everestengg.code.challenge.model.PackageChargeInformation;

public interface PackageOrderService {
    /**
     *
     * @param inputPackage packages to be delivered
     * @param baseDeliveryCost base delivery cost
     * @return PackageChargeInformation contains charges, discount and final charges
     */
    PackageChargeInformation calcCost(InputPackage inputPackage, float baseDeliveryCost);


}
