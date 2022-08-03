package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.bo.DeliveryInput;
import com.everestengg.code.challenge.bo.InputPackage;
import com.everestengg.code.challenge.model.DeliveryOrder;

import java.util.List;

public interface DeliveryEstimationService {
     /**
      * calculates estimated delivery
      * @param inputPackages packages to be delivered
      * @param deliveryInput captures information like number of available vehicles, max speed, carriable weight
      * @param baseDeliveryCost base delivery cost
      * @return list of packages to be delivered and time calculated for each package
      */
     List<DeliveryOrder> calculateEstimatedDelivery(InputPackage[] inputPackages, DeliveryInput deliveryInput,
                                                    short baseDeliveryCost);
}
