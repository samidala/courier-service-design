package com.everestengg.code.challenge.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PackageChargeInformation {
    private String packageId;
    private double totalDiscount;
    private double totalCost;
    private double costAfterDiscount;

    public void print(){
        System.out.println(this);
    }

    @Override
    public String toString() {
        return packageId + " "+totalDiscount + " "+costAfterDiscount;
    }
}