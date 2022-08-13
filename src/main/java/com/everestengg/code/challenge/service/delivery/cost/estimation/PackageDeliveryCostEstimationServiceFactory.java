package com.everestengg.code.challenge.service.delivery.cost.estimation;

/**
 * factory class for @{@link PackageDeliveryCostEstimationService} implementations
 */
public class PackageDeliveryCostEstimationServiceFactory {

    private PackageDeliveryCostEstimationServiceFactory(){}
    public enum PackageServiceType{
        SIMPLE;
    }
    public static PackageDeliveryCostEstimationService getPackageOrderService(PackageServiceType packageServiceType){
        PackageDeliveryCostEstimationService packageDeliveryCostEstimationService;
        switch (packageServiceType){
            case SIMPLE:
                packageDeliveryCostEstimationService = new PackageDeliveryCostEstimationImpl();
                break;
            default: throw new IllegalArgumentException("invalid type "+packageServiceType);
        }
        return packageDeliveryCostEstimationService;

    }
}
