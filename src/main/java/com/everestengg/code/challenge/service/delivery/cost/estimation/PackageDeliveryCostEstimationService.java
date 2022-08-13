package com.everestengg.code.challenge.service.delivery.cost.estimation;

import com.everestengg.code.challenge.model.PackageDeliveryCostEstimateInfo;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Response;

public interface PackageDeliveryCostEstimationService {
    /**
     *
     * @param inputPackage packages to be delivered
     * @param baseDeliveryCost base delivery cost
     * @return PackageChargeInformation contains charges, discount and final charges
     */
    Response<PackageDeliveryCostEstimateInfo> calcCost(InputPackage inputPackage, float baseDeliveryCost);


}
