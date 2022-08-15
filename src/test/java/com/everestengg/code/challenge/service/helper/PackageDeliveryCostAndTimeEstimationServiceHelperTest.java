package com.everestengg.code.challenge.service.helper;


import com.everestengg.code.challenge.exceptions.InvalidValueException;
import com.everestengg.code.challenge.model.courier.PackageDeliveryCostAndTimeEstimationInfo;
import com.everestengg.code.challenge.repo.offer.StaticOfferRepository;
import com.everestengg.code.challenge.service.delivery.helper.PackageDeliveryCostAndTimeEstimationServiceHelper;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Package;
import com.everestengg.code.challenge.vo.PackageDeliveryInput;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackageDeliveryCostAndTimeEstimationServiceHelperTest {

    @BeforeAll
    public static void init(){
        StaticOfferRepository.prepareOffers();
    }

    private PackageDeliveryCostAndTimeEstimationServiceHelper getDeliveryCostAndTimeEstimationServiceHelper(){
        return new PackageDeliveryCostAndTimeEstimationServiceHelper();
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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);

        assertEquals(5,packageDeliveryCostAndTimeEstimationInfos.size());

        double[] expectedDeliveryTimes = {3.98,1.78,1.42,0.85,4.19};
        double[] expectedCosts = {750,1475,2350,1395,2125};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);
    }

    private void assertValues(String[] pkgIds,
                              List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos,
                              double[] expectedDeliveryTimes, double[] expectedCosts) {
        for(int i = 0 ; i < packageDeliveryCostAndTimeEstimationInfos.size(); i++){
            PackageDeliveryCostAndTimeEstimationInfo item = packageDeliveryCostAndTimeEstimationInfos.get(i);
            assertEquals(pkgIds[i],item.getPackageDeliveryCostEstimateInfo().getPackageId());
            assertEquals(expectedDeliveryTimes[i],item.getEstimatedDeliveryTime());
            assertEquals(expectedCosts[i],item.getPackageDeliveryCostEstimateInfo().getCostAfterDiscount());
        }
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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {0.42,1.78,1.42,0.85,1.35,2.98};
        double[] expectedCosts = {750,1225,2350,900,845,410};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {0.42,1.78,1.7,0.85,1.35,0.14};
        double[] expectedCosts = {750,1225,2350,900,845,2150};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {0.42,1.78,1.42,0.85,1.35,0.14};
        double[] expectedCosts = {750,1225,2350,900,825,400};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {0.42,1.78,2.02,0.85,1.35,0.14,0.3};
        double[] expectedCosts = {750,1225,2350,900,825,400,2105};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {0.7,5.6,3.72,1.45,2.47,0.14,0.3};
        double[] expectedCosts = {2025,2725,2600,2400,2575,2150,2205};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {4,3.48,5.84,0.85,1.95,3.44,0.3};
        double[] expectedCosts = {2016,2725,2590,2400,2575,2140,2205};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);
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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {3.72,4.62,1.42,4.99,1.95,0.14,0.3};
        double[] expectedCosts = {2016,2725,2590,2390,2575,160,2205};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

    }

    private List<PackageDeliveryCostAndTimeEstimationInfo> calcEstimatedDeliveryAndPrint(InputPackage[] inputPackages, PackageDeliveryInput packageDeliveryInput) {
        List<PackageDeliveryCostAndTimeEstimationInfo> deliveries = getDeliveryCostAndTimeEstimationServiceHelper()
                .calculateEstimatedDelivery(inputPackages, packageDeliveryInput, (short)100);
        //Collections.sort(deliveries, Comparator.comparing(o -> o.getPackageDeliveryCostEstimateInfo().getPackageId()));
        for (PackageDeliveryCostAndTimeEstimationInfo packageDeliveryCostAndTimeEstimationInfo : deliveries){
            System.out.println(packageDeliveryCostAndTimeEstimationInfo);
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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {0.42,1.78,1.42,0.85,1.35,0.14,0.3};
        double[] expectedCosts = {2025,2725,2600,2400,2575,2150,2205};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {4.96,4.48,3.12,0.85,1.35};
        double[] expectedCosts = {1971,2675,2560,2204.10009765625,2555};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

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

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfos =
                calcEstimatedDeliveryAndPrint(inputPackages, packageDeliveryInput);
        double[] expectedDeliveryTimes = {4.96,4.48,3.12,0.85,1.35};
        double[] expectedCosts = {1971,2675,2560,2194.800048828125,2555};
        assertValues(pkgIds, packageDeliveryCostAndTimeEstimationInfos, expectedDeliveryTimes, expectedCosts);

    }
    @Test
    void testNegativeAvailableVehicles() {
        InputPackage[] inputPackages = getDummyInputPackages();
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) -2).build();
        InvalidValueException thrown = assertThrows(
                InvalidValueException.class,
                () -> {
                    getDeliveryCostAndTimeEstimationServiceHelper()
                            .calculateEstimatedDelivery(inputPackages, packageDeliveryInput, (short)100);
                });
        assertEquals("invalid value for number of vehicles -2", thrown.getMessage());
    }

    @Test
    void testNegativeMaxSpeed() {
        InputPackage[] inputPackages = getDummyInputPackages();
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) -70).noOfVehicle((short) 2).build();
        InvalidValueException thrown = assertThrows(
                InvalidValueException.class,
                () -> {
                    getDeliveryCostAndTimeEstimationServiceHelper()
                            .calculateEstimatedDelivery(inputPackages, packageDeliveryInput, (short)100);
                });
        assertEquals("invalid value for max speed -70", thrown.getMessage());
    }

    @Test
    void testNegativeCarriableMaxWt() {
        InputPackage[] inputPackages = getDummyInputPackages();
        PackageDeliveryInput packageDeliveryInput = PackageDeliveryInput.builder().maxCarriableWt((short) -19)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();
        InvalidValueException thrown = assertThrows(
                InvalidValueException.class,
                () -> {
                    getDeliveryCostAndTimeEstimationServiceHelper()
                            .calculateEstimatedDelivery(inputPackages, packageDeliveryInput, (short)100);
                });
        assertEquals("invalid value for max carriable weight -19", thrown.getMessage());
    }

    private InputPackage[] getDummyInputPackages() {
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
        return inputPackages;
    }

}
