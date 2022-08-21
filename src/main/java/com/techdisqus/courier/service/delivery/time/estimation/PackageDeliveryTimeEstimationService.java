package com.techdisqus.courier.service.delivery.time.estimation;

import com.techdisqus.courier.vo.courier.CourierRequest;
import com.techdisqus.courier.vo.VehicleInformation;
import com.techdisqus.courier.vo.courier.CourierResponse;

import java.util.List;

public interface PackageDeliveryTimeEstimationService {
     /**
      * calculates estimated delivery
      * @param courierRequests packages to be delivered
      * @param vehicleInformation captures information like number of available vehicles, max speed, carriable weight
      * @param baseDeliveryCost base delivery cost
      * @return list of packages to be delivered and time calculated for each package
      */
     List<CourierResponse> calculateEstimatedDelivery(CourierRequest[] courierRequests,
                                                      VehicleInformation vehicleInformation,
                                                      short baseDeliveryCost);
}
