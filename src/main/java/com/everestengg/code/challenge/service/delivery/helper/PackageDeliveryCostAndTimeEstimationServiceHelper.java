package com.everestengg.code.challenge.service.delivery.helper;

import com.everestengg.code.challenge.vo.courier.CourierResponse;
import com.everestengg.code.challenge.vo.courier.PackageDeliveryCostEstimateInfo;
import com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationImpl;
import com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationService;
import com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationService;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationServiceFactory;
import com.everestengg.code.challenge.vo.courier.CourierRequest;
import com.everestengg.code.challenge.vo.VehicleInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory.PackageServiceType.SIMPLE;

public class PackageDeliveryCostAndTimeEstimationServiceHelper {

    public List<CourierResponse> calculateEstimatedDelivery(CourierRequest[] courierRequests,
                                                            VehicleInformation vehicleInformation,
                                                            short baseDeliveryCost){

        List<PackageDeliveryCostEstimationImpl.InputPackageValidator> validatorList =
                getPackageDeliveryCostEstimationService().getInputPackageValidatorList();
        Arrays.stream(courierRequests).forEach(item -> validatorList.forEach(e-> e.validate(item)));

        List<CourierResponse> courierResponseList =
                getDeliveryTimeEstimationService().calculateEstimatedDelivery(courierRequests,
                        vehicleInformation, baseDeliveryCost);

        return calculatePackageDeliveryCostEstimation(courierRequests, courierResponseList,
                baseDeliveryCost);

    }

    private PackageDeliveryCostEstimationService getPackageDeliveryCostEstimationService() {
        return PackageDeliveryCostEstimationServiceFactory
                .getPackageDeliveryCostEstimationService(SIMPLE);
    }

    private PackageDeliveryTimeEstimationService getDeliveryTimeEstimationService() {
        return PackageDeliveryTimeEstimationServiceFactory
                .getDeliveryEstimationService(PackageDeliveryTimeEstimationServiceFactory
                        .PackageDeliveryTimeEstimationType.SIMPLE);
    }


    /**
     * @param courierRequests                            packages to be delivered
     * @param courierResponse calculated @{@link CourierResponse}
     * @param baseDeliveryCost
     * @return package delivery order @{@link CourierResponse}
     */
    public List<CourierResponse> calculatePackageDeliveryCostEstimation(CourierRequest[] courierRequests,
                                                                        List<CourierResponse>
                                                                                courierResponse,
                                                                        short baseDeliveryCost) {
        Map<String, CourierResponse> result =  courierResponse.stream()
                .collect(Collectors.toMap(CourierResponse::getPackageId, Function.identity()));

        List<CourierResponse> courierResponses = new ArrayList<>(courierRequests.length);
        for(CourierRequest courierRequest : courierRequests){
            PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = getPackageDeliveryCostEstimationService()
                    .calcCost(courierRequest, baseDeliveryCost).getResult();
            courierResponses.add(result.get(
                            courierRequest.getPackageDetails().getPackageId()).toBuilder()
                    .packageDeliveryCostEstimateInfo(packageDeliveryCostEstimateInfo).build());

        }
        return courierResponses;
    }
}
