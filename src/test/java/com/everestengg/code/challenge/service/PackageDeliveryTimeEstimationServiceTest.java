package com.everestengg.code.challenge.service;


import com.everestengg.code.challenge.model.PackageDeliveryTimeEstimationInfo;
import com.everestengg.code.challenge.repo.StaticOfferRepository;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationService;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationServiceFactory;
import com.everestengg.code.challenge.vo.PackageDeliveryInput;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Package;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationServiceFactory.PackageDeliveryTimeEstimationType.SIMPLE;

public class PackageDeliveryTimeEstimationServiceTest {

    @BeforeAll
    public static void init(){
        StaticOfferRepository.prepareOffers();
    }

    private PackageDeliveryTimeEstimationService getDeliveryEstimationServiceFactory(){
        return PackageDeliveryTimeEstimationServiceFactory.getDeliveryEstimationService(SIMPLE);
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
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

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
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDelivery1(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6"};
        short[] wts = {50,50,175,50,27,200};
        short[] dists = {30,125,100,60,95,10};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA"};
        InputPackage[] inputPackages = new InputPackage[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

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
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

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
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

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
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

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
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

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
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

    }

    private List<PackageDeliveryTimeEstimationInfo> calcEstimatedDeliveryAndPrint(InputPackage[] inputPackages, PackageDeliveryInput packageDeliveryInput) {
        List<PackageDeliveryTimeEstimationInfo> deliveries = getDeliveryEstimationServiceFactory()
                .calculateEstimatedDelivery(inputPackages, packageDeliveryInput, (short)100);
        Collections.sort(deliveries, Comparator.comparing(o -> o.getPackageDeliveryCostEstimateInfo().getPackageId()));
        for (PackageDeliveryTimeEstimationInfo packageDeliveryTimeEstimationInfo : deliveries){
            System.out.println(packageDeliveryTimeEstimationInfo);
        }
        return deliveries;
    }

    @Test
    void testCalculateEstimatedDeliveryOfMaxWeightsAndHavingMoreVehicles(){
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
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 10).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfConsequentWts(){

        InputPackage[] inputPackages = new InputPackage[5];
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5"};
        short[] wts = {194,195,196,197,198};
        short[] dists = {30,125,100,60,95};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFR002","NA"};
        for(int i = 0 ; i < 5; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

    }

    @Test
    void testCalculateEstimatedDeliveryOfConsequentWtsWithDuplicate(){

        InputPackage[] inputPackages = new InputPackage[5];
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5"};
        short[] wts = {194,195,196,196,198};
        short[] dists = {30,125,100,60,95};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFR002","NA"};
        for(int i = 0 ; i < 5; i++){
            inputPackages[i] = new InputPackage(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

    }
}
