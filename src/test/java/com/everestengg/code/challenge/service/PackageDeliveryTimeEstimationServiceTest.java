package com.everestengg.code.challenge.service;


import com.everestengg.code.challenge.model.PackageDeliveryTimeEstimationInfo;
import com.everestengg.code.challenge.repo.StaticOfferRepository;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationService;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationServiceFactory;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Package;
import com.everestengg.code.challenge.vo.PackageDeliveryInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.PriorityQueue;

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
    void testPq(){
        PriorityQueue<VehicleAvailability> pq = new PriorityQueue<>(2,
                (o1, o2) -> {
                    double o1DeliveryTime = o1.getDeliveryTime();
                    double o2DeliveryTime = o2.getDeliveryTime();
                    double result = Math.ceil(o1DeliveryTime) - Math.ceil(o2DeliveryTime);
                    System.out.println("result "+result);

                    return (int) result;
                });
        for(int i = 1; i <= 2;i++){
            pq.offer(VehicleAvailability.builder().vehicleNo((short) i).deliveryTime(0).build());
        }

        System.out.println(pq);
        VehicleAvailability item = pq.poll();
        System.out.println(item);
        VehicleAvailability item1 = VehicleAvailability.builder().vehicleNo((short) 1).deliveryTime(3.56).build();
        pq.offer(item1);
        System.out.println(pq);
        item = pq.poll();
        System.out.println(item);

        VehicleAvailability item2 = VehicleAvailability.builder().vehicleNo((short) 2).deliveryTime(2.84).build();
        pq.offer(item2);

        System.out.println(pq);
        item = pq.poll();
        System.out.println(item);

        /*while(true){
            VehicleAvailability item = pq.poll();
            System.out.println(item);
            VehicleAvailability item1 = VehicleAvailability.builder().vehicleNo((short) 1).deliveryTime(3.56).build();
            pq.offer(item1);
        }*/

    }

    @Builder
    @AllArgsConstructor
    @Getter
    @ToString
    private static class VehicleAvailability{
        private final short vehicleNo;
        private final double deliveryTime;
    }
    @Test
    public void testDouble(){
        double dist = 125;
        double maxSpeed = 70;
        double res1 = dist/maxSpeed;
        System.out.println("res1 "+res1);

        double dist1 = 100;
        double res2 = dist1 / maxSpeed;
        System.out.println("res2 "+res2);

        System.out.println("round res2 "+ (Math.round(res2 * 100)/100));
        double dist3 = 95;
        double res3 = dist3 / maxSpeed;
        System.out.println("res3 "+res3);
        System.out.println("round res3 "+ (Math.round(res3 * 100)/100));
        System.out.println("res2 * 2 = "+ ((res2 * 2) + (res3 * 2)));
        final DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.FLOOR);
        System.out.println("formatting");
        System.out.println(res2 + "  "+(Double.parseDouble(df.format(res2))));
        System.out.println(res3 + "  "+(Double.parseDouble(df.format(res3))));

        System.out.println((Double.parseDouble(df.format(res2)) * 2) + (Double.parseDouble(df.format(res3)) * 2) );
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
        //Collections.sort(deliveries, Comparator.comparing(o -> o.getPackageDeliveryCostEstimateInfo().getPackageId()));
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
