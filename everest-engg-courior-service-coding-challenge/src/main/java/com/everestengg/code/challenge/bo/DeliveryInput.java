package com.everestengg.code.challenge.bo;

import lombok.Builder;
import lombok.Getter;

/**
 * captures number of vehicles available for delivery, max speed and max carriable weight
 */
@Builder
@Getter
public class DeliveryInput {

    private short noOfVehicle;
    private short maxSpeed;
    private short maxCarriableWt;
}
