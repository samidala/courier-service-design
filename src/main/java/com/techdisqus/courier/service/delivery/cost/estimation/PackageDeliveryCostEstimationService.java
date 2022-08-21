package com.techdisqus.courier.service.delivery.cost.estimation;

import com.techdisqus.courier.vo.courier.PackageDeliveryCostEstimateInfo;
import com.techdisqus.courier.vo.courier.CourierRequest;
import com.techdisqus.courier.vo.Response;

import java.util.List;

public interface PackageDeliveryCostEstimationService {
    List<PackageDeliveryCostEstimationImpl.InputPackageValidator> getInputPackageValidatorList();

    /**
     *
     * @param courierRequest packages to be delivered
     * @param baseDeliveryCost base delivery cost
     * @return PackageChargeInformation contains charges, discount and final charges
     */
    Response<PackageDeliveryCostEstimateInfo> calcCost(CourierRequest courierRequest, float baseDeliveryCost);


}
