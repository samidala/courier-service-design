package com.techdisqus.courier.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * captures number of vehicles available for delivery, max speed and max carriable weight
 */
@Builder
@Getter
public class VehicleInformation {

    private short noOfVehicle;
    private short maxSpeed;
    private short maxCarriableWt;

    public boolean isNoOfVehicleValid(){
        return noOfVehicle > 0;
    }
    public boolean isValidMaxSpeed(){
        return maxSpeed > 0;
    }
    public boolean isValidMaxCarriableWt(){
        return maxCarriableWt > 0;
    }
}
