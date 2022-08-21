package com.everestengg.code.challenge.service;


import com.everestengg.code.challenge.exceptions.InvalidValueException;
import com.everestengg.code.challenge.vo.courier.CourierResponse;
import com.everestengg.code.challenge.repo.offer.StaticOfferRepository;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationService;
import com.everestengg.code.challenge.service.delivery.time.estimation.PackageDeliveryTimeEstimationServiceFactory;
import com.everestengg.code.challenge.vo.courier.CourierRequest;
import com.everestengg.code.challenge.vo.courier.Package;
import com.everestengg.code.challenge.vo.VehicleInformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackageDeliveryTimeEstimationServiceImplTest {

    @BeforeAll
    public static void init(){
        StaticOfferRepository.prepareOffers();
    }

    private PackageDeliveryTimeEstimationService getDeliveryTimeEstimationService(){
        return  PackageDeliveryTimeEstimationServiceFactory
                .getDeliveryEstimationService(PackageDeliveryTimeEstimationServiceFactory
                        .PackageDeliveryTimeEstimationType.SIMPLE);
    }
    @Test
    void testCalculateEstimatedDeliveryOfDiffWeights(){

        CourierRequest[] courierRequests = new CourierRequest[5];
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5"};
        short[] wts = {50,75,175,110,155};
        short[] dists = {30,125,100,60,95};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFR002","NA"};
        for(int i = 0 ; i < 5; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);

        assertEquals(5, courierResponses.size());

        double[] expectedDeliveryTimes = {3.98,1.78,1.42,0.85,4.19};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);
    }

    private void assertValues(String[] pkgIds,
                              List<CourierResponse> courierResponses,
                              double[] expectedDeliveryTimes) {
        for(int i = 0; i < courierResponses.size(); i++){
            CourierResponse item = courierResponses.get(i);
            assertEquals(pkgIds[i],item.getPackageId());
            assertEquals(expectedDeliveryTimes[i],item.getEstimatedDeliveryTime());
        }
    }

    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDelivery(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6"};
        short[] wts = {50,50,175,50,27,26};
        short[] dists = {30,125,100,60,95,10};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA"};
        CourierRequest[] courierRequests = new CourierRequest[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {0.42,1.78,1.42,0.85,1.35,2.98};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }

    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDelivery1(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6"};
        short[] wts = {50,50,175,50,27,200};
        short[] dists = {30,125,100,60,95,10};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA"};
        CourierRequest[] courierRequests = new CourierRequest[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {0.42,1.78,1.7,0.85,1.35,0.14};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }

    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDeliveryAndHaveSimilarWeights(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6"};
        short[] wts = {50,50,175,50,25,25};
        short[] dists = {30,125,100,60,95,10};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA"};
        CourierRequest[] courierRequests = new CourierRequest[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {0.42,1.78,1.42,0.85,1.35,0.14};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }

    @Test
    void testCalculateEstimatedDeliveryOfDiffWeightsAndMixOfPkgDeliveryAndHaveSimilarWeights1(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {50,50,175,50,25,25,190};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        CourierRequest[] courierRequests = new CourierRequest[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {0.42,1.78,2.02,0.85,1.35,0.14,0.3};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }

    @Test
    void testCalculateEstimatedDeliveryOfMaxWeights(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {200,200,200,200,200,200,200};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        CourierRequest[] courierRequests = new CourierRequest[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {0.7,5.6,3.72,1.45,2.47,0.14,0.3};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }

    @Test
    void testCalculateEstimatedDeliveryOfMaxWeights199And200(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {199,200,199,200,200,199,200};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        CourierRequest[] courierRequests = new CourierRequest[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {4,3.48,5.84,0.85,1.95,3.44,0.3};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);
    }

    @Test
    void testCalculateEstimatedDeliveryOfMaxWeights199And200AndOne(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {199,200,199,199,200,1,200};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        CourierRequest[] courierRequests = new CourierRequest[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {3.72,4.62,1.42,4.99,1.95,0.14,0.3};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }

    private List<CourierResponse> calcEstimatedDeliveryAndPrint(CourierRequest[] courierRequests, VehicleInformation vehicleInformation) {
        List<CourierResponse> deliveries = getDeliveryTimeEstimationService()
                .calculateEstimatedDelivery(courierRequests, vehicleInformation, (short)100);
        return deliveries;
    }

    @Test
    void testCalculateEstimatedDeliveryOfMaxWeightsAndHavingMoreVehicles(){
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5","PKG6","pkg7"};
        short[] wts = {200,200,200,200,200,200,200};
        short[] dists = {30,125,100,60,95,10,21};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFFR002","NA","NA","NA"};
        CourierRequest[] courierRequests = new CourierRequest[pkgIds.length];
        for(int i = 0 ; i < pkgIds.length; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 10).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {0.42,1.78,1.42,0.85,1.35,0.14,0.3};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }

    @Test
    void testCalculateEstimatedDeliveryOfConsequentWts(){

        CourierRequest[] courierRequests = new CourierRequest[5];
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5"};
        short[] wts = {194,195,196,197,198};
        short[] dists = {30,125,100,60,95};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFR002","NA"};
        for(int i = 0 ; i < 5; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {4.96,4.48,3.12,0.85,1.35};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }

    @Test
    void testCalculateEstimatedDeliveryOfConsequentWtsWithDuplicate(){

        CourierRequest[] courierRequests = new CourierRequest[5];
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5"};
        short[] wts = {194,195,196,196,198};
        short[] dists = {30,125,100,60,95};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFR002","NA"};
        for(int i = 0 ; i < 5; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        List<CourierResponse> courierResponses =
                calcEstimatedDeliveryAndPrint(courierRequests, vehicleInformation);
        double[] expectedDeliveryTimes = {4.96,4.48,3.12,0.85,1.35};
        assertValues(pkgIds, courierResponses, expectedDeliveryTimes);

    }


    @Test
    void testCalculateEstimatedDeliveryWithWtMoreThanAllowed(){

        CourierRequest[] courierRequests = new CourierRequest[5];
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5"};
        short[] wts = {194,195,196,196,220};
        short[] dists = {30,125,100,60,95};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFR002","NA"};
        for(int i = 0 ; i < 5; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();

        InvalidValueException thrown = assertThrows(
                InvalidValueException.class,
                () -> {
                    getDeliveryTimeEstimationService()
                            .calculateEstimatedDelivery(courierRequests, vehicleInformation, (short)100);
                });
        assertEquals("package weight is 220 more than deliverable weight 200", thrown.getMessage());

    }
    @Test
    void testNegativeAvailableVehicles() {
        CourierRequest[] courierRequests = getDummyInputPackages();
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) -2).build();
        InvalidValueException thrown = assertThrows(
                InvalidValueException.class,
                () -> {
                    getDeliveryTimeEstimationService()
                            .calculateEstimatedDelivery(courierRequests, vehicleInformation, (short)100);
                });
        assertEquals("invalid value for number of vehicles -2", thrown.getMessage());
    }

    @Test
    void testNegativeMaxSpeed() {
        CourierRequest[] courierRequests = getDummyInputPackages();
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) -70).noOfVehicle((short) 2).build();
        InvalidValueException thrown = assertThrows(
                InvalidValueException.class,
                () -> {
                    getDeliveryTimeEstimationService()
                            .calculateEstimatedDelivery(courierRequests, vehicleInformation, (short)100);
                });
        assertEquals("invalid value for max speed -70", thrown.getMessage());
    }

    @Test
    void testInputPackagesNull() {
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) -70).noOfVehicle((short) 2).build();
        AssertionError thrown = assertThrows(
                AssertionError.class,
                () -> {
                    getDeliveryTimeEstimationService()
                            .calculateEstimatedDelivery(null, vehicleInformation, (short)100);
                });
        assertEquals("input packages should not be null", thrown.getMessage());
    }

    @Test
    void testPackagesNull() {
        CourierRequest[] courierRequests = new CourierRequest[1];
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) -70).noOfVehicle((short) 2).build();
        AssertionError thrown = assertThrows(
                AssertionError.class,
                () -> {
                    getDeliveryTimeEstimationService()
                            .calculateEstimatedDelivery(courierRequests, vehicleInformation, (short)100);
                });
        assertEquals("package should not be null", thrown.getMessage());
    }

    @Test
    void testPackageDeliveryInputNull() {
        CourierRequest[] courierRequests = getDummyInputPackages();
        AssertionError thrown = assertThrows(
                AssertionError.class,
                () -> {
                    getDeliveryTimeEstimationService()
                            .calculateEstimatedDelivery(courierRequests, null, (short)100);
                });
        assertEquals("package delivery input should not be null", thrown.getMessage());
    }
    @Test
    void testBaseDeliveryCostValueNegative() {
        CourierRequest[] courierRequests = getDummyInputPackages();
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) 200)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();
        AssertionError thrown = assertThrows(
                AssertionError.class,
                () -> {
                    getDeliveryTimeEstimationService()
                            .calculateEstimatedDelivery(courierRequests, vehicleInformation, (short)-100);
                });
        assertEquals("base delivery cost should be between 1 and 32767", thrown.getMessage());
    }
    @Test
    void testNegativeCarriableMaxWt() {
        CourierRequest[] courierRequests = getDummyInputPackages();
        VehicleInformation vehicleInformation = VehicleInformation.builder().maxCarriableWt((short) -19)
                .maxSpeed((short) 70).noOfVehicle((short) 2).build();
        InvalidValueException thrown = assertThrows(
                InvalidValueException.class,
                () -> {
                    getDeliveryTimeEstimationService()
                            .calculateEstimatedDelivery(courierRequests, vehicleInformation, (short)100);
                });
        assertEquals("invalid value for max carriable weight -19", thrown.getMessage());
    }

    private CourierRequest[] getDummyInputPackages() {
        CourierRequest[] courierRequests = new CourierRequest[5];
        String[] pkgIds = {"PKG1","PKG2","PKG3","PKG4","PKG5"};
        short[] wts = {194,195,196,196,198};
        short[] dists = {30,125,100,60,95};
        String[] offrCodes = {"OFR001","OFFR0008","OFFR003","OFR002","NA"};
        for(int i = 0 ; i < 5; i++){
            courierRequests[i] = new CourierRequest(
                    Package.builder().packageId(pkgIds[i])
                            .weight(wts[i]).dist(dists[i]).build(), offrCodes[i]);

        }
        return courierRequests;
    }

}
