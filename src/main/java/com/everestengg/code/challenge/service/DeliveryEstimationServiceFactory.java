package com.everestengg.code.challenge.service;

/**
 * factory class to get the {@link DeliveryEstimationService} implementations
 */
public class DeliveryEstimationServiceFactory {

    private DeliveryEstimationServiceFactory(){}

    public enum DeliveryEstimationType{
        SIMPLE;
    }
    public static DeliveryEstimationService getDeliveryEstimationService(DeliveryEstimationType deliveryEstimationType){
        DeliveryEstimationService deliveryEstimationService;
        switch (deliveryEstimationType){
            case SIMPLE:
                deliveryEstimationService = new DeliveryEstimationServiceImpl();
                break;
            default: throw new IllegalArgumentException("Invalid type "+deliveryEstimationType);
        }
        return deliveryEstimationService;
    }
}
