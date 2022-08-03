package com.everestengg.code.challenge.service;


import com.everestengg.code.challenge.model.DeliveryOrder;
import com.everestengg.code.challenge.util.Utils;
import com.everestengg.code.challenge.bo.DeliveryInput;
import com.everestengg.code.challenge.bo.InputPackage;
import com.everestengg.code.challenge.bo.Package;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.everestengg.code.challenge.service.DeliveryEstimationServiceFactory.DeliveryEstimationType.SIMPLE;

public class DeliveryEstimationServiceTest {

    @BeforeAll
    public static void init(){
        Utils.prepareOffers();
    }

    private DeliveryEstimationService getDeliveryEstimationServiceFactory(){
        return DeliveryEstimationServiceFactory.getDeliveryEstimationService(SIMPLE);
    }
    @Test
    void testCalculateEstimatedDeliveryOfDiffWeights(){

        InputPackage[] inputPackages = new InputPackage[5];
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5"};
        short[] wts = {50,75,175,110,155};
        short[] dists = {30,125,100,60,95};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFR002","NA"};
        for(int i = 0 ; i < 5; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        DeliveryInput deliveryInput = DeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages,deliveryInput);

    }


    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDelivery(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6"};
        short[] wts = {50,50,175,50,27,26};
        short[] dists = {30,125,100,60,95,10};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA"};
        InputPackage[] inputPackages = new InputPackage[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        DeliveryInput deliveryInput = DeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, deliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDelivery1(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6"};
        short[] wts = {50,50,175,50,27,26};
        short[] dists = {30,125,100,60,95,10};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA"};
        InputPackage[] inputPackages = new InputPackage[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        DeliveryInput deliveryInput = DeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, deliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDeliveryAndHaveSimilarWeights(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6"};
        short[] wts = {50,50,175,50,25,25};
        short[] dists = {30,125,100,60,95,10};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA"};
        InputPackage[] inputPackages = new InputPackage[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        DeliveryInput deliveryInput = DeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, deliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDeliveryAndHaveSimilarWeights1(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {50,50,175,50,25,25,190};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        InputPackage[] inputPackages = new InputPackage[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        DeliveryInput deliveryInput = DeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, deliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfMaxWeights(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {200,200,200,200,200,200,200};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        InputPackage[] inputPackages = new InputPackage[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        DeliveryInput deliveryInput = DeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, deliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfMaxWeights199And200(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {199,200,199,200,200,199,200};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        InputPackage[] inputPackages = new InputPackage[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        DeliveryInput deliveryInput = DeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, deliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfMaxWeights199And200AndOne(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {199,200,199,199,200,1,200};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        InputPackage[] inputPackages = new InputPackage[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        DeliveryInput deliveryInput = DeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, deliveryInput);

    }

    private List<DeliveryOrder> calcEstimatedDeliveryAndPrint(InputPackage[] inputPackages, DeliveryInput deliveryInput) {
        List<DeliveryOrder> deliveries = getDeliveryEstimationServiceFactory()
                .calculateEstimatedDelivery(inputPackages, deliveryInput, (short)100);
        for (DeliveryOrder deliveryOrder : deliveries){
            System.out.println(deliveryOrder);
        }
        return deliveries;
    }
}
