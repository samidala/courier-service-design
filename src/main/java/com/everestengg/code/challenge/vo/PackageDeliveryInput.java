package com.everestengg.code.challenge.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * captures number of vehicles available for delivery, max speed and max carriable weight
 */
@Builder
@Getter
public class PackageDeliveryInput {

    private short noOfVehicle;
    private short maxSpeed;
    private short maxCarriableWt;
}
