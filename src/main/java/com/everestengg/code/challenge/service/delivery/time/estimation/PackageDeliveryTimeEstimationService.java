package com.everestengg.code.challenge.service.delivery.time.estimation;

import com.everestengg.code.challenge.vo.PackageDeliveryInput;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.model.PackageDeliveryTimeEstimationInfo;

import java.util.List;

public interface PackageDeliveryTimeEstimationService {
     /**
      * calculates estimated delivery
      * @param inputPackages packages to be delivered
      * @param packageDeliveryInput captures information like number of available vehicles, max speed, carriable weight
      * @param baseDeliveryCost base delivery cost
      * @return list of packages to be delivered and time calculated for each package
      */
     List<PackageDeliveryTimeEstimationInfo> calculateEstimatedDelivery(InputPackage[] inputPackages, PackageDeliveryInput packageDeliveryInput,
                                                                        short baseDeliveryCost);
}
