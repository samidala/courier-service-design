package com.everestengg.code.challenge.service.delivery.time.estimation;

/**
 * factory class to get the {@link PackageDeliveryTimeEstimationService} implementations
 */
public class PackageDeliveryTimeEstimationServiceFactory {

    private PackageDeliveryTimeEstimationServiceFactory(){}

    public enum PackageDeliveryTimeEstimationType {
        SIMPLE;
    }
    public static PackageDeliveryTimeEstimationService getDeliveryEstimationService(PackageDeliveryTimeEstimationType packageDeliveryTimeEstimationType){
        PackageDeliveryTimeEstimationService packageDeliveryTimeEstimationService;
        switch (packageDeliveryTimeEstimationType){
            case SIMPLE:
                packageDeliveryTimeEstimationService = new PackageDeliveryTimeEstimationServiceImpl();
                break;
            default: throw new IllegalArgumentException("Invalid type "+ packageDeliveryTimeEstimationType);
        }
        return packageDeliveryTimeEstimationService;
    }
}
