package com.everestengg.code.challenge.service.delivery.helper;

import com.everestengg.code.challenge.model.courier.PackageDeliveryCostAndTimeEstimationInfo;
import com.everestengg.code.challenge.model.courier.PackageDeliveryCostEstimateInfo;
import com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationServiceFactory;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.PackageDeliveryInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.everestengg.code.challenge.service.delivery.cost.estimation.PackageDeliveryCostEstimationServiceFactory.PackageServiceType.SIMPLE;

public class PackageDeliveryCostAndTimeEstimationServiceHelper {

    public List<PackageDeliveryCostAndTimeEstimationInfo> calculateEstimatedDelivery(InputPackage[] inputPackages,
                                                                                     PackageDeliveryInput packageDeliveryInput,
                                                                                     short baseDeliveryCost){
        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfoList =
                PackageDeliveryTimeEstimationServiceFactory
                .getDeliveryEstimationService(PackageDeliveryTimeEstimationServiceFactory
                        .PackageDeliveryTimeEstimationType.SIMPLE).calculateEstimatedDelivery(inputPackages,
                                packageDeliveryInput, baseDeliveryCost);

        return calculatePackageDeliveryCostEstimation(inputPackages,packageDeliveryCostAndTimeEstimationInfoList,
                baseDeliveryCost);

    }


    /**
     * @param inputPackages                            packages to be delivered
     * @param packageDeliveryCostAndTimeEstimationInfo calculated @{@link PackageDeliveryCostAndTimeEstimationInfo}
     * @param baseDeliveryCost
     * @return package delivery order @{@link PackageDeliveryCostAndTimeEstimationInfo}
     */
    public List<PackageDeliveryCostAndTimeEstimationInfo> calculatePackageDeliveryCostEstimation(InputPackage[] inputPackages,
                                                                                                  List<PackageDeliveryCostAndTimeEstimationInfo>
                                                                                                          packageDeliveryCostAndTimeEstimationInfo,
                                                                                                  short baseDeliveryCost) {
        Map<String, PackageDeliveryCostAndTimeEstimationInfo> result =  packageDeliveryCostAndTimeEstimationInfo.stream()
                .collect(Collectors.toMap(PackageDeliveryCostAndTimeEstimationInfo::getPackageId, Function.identity()));

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos = new ArrayList<>(inputPackages.length);
        for(InputPackage inputPackage : inputPackages){
            PackageDeliveryCostEstimateInfo packageDeliveryCostEstimateInfo = PackageDeliveryCostEstimationServiceFactory
                    .getPackageOrderService(SIMPLE)
                    .calcCost(inputPackage, baseDeliveryCost).getResult();
            packageDeliveryCostAndTimeEstimationInfos.add(result.get(
                            inputPackage.getPackageDetails().getPackageId()).toBuilder()
                    .packageDeliveryCostEstimateInfo(packageDeliveryCostEstimateInfo).build());

        }
        return packageDeliveryCostAndTimeEstimationInfos;
    }
}
