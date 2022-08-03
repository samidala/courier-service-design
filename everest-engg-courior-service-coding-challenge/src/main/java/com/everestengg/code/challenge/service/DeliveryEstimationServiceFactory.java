package com.everestengg.code.challenge.service;

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
