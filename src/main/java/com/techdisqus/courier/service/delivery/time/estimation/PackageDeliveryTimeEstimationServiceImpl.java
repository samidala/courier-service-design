package com.techdisqus.courier.service.delivery.time.estimation;

import com.techdisqus.courier.exceptions.InvalidValueException;
import com.techdisqus.courier.vo.courier.CourierResponse;
import com.techdisqus.courier.vo.courier.CourierRequest;
import com.techdisqus.courier.vo.VehicleInformation;
import com.techdisqus.courier.vo.courier.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PackageDeliveryTimeEstimationServiceImpl implements PackageDeliveryTimeEstimationService {

    private static Logger LOGGER = LoggerFactory.getLogger(PackageDeliveryTimeEstimationServiceImpl.class);
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public PackageDeliveryTimeEstimationServiceImpl(){
        df.setRoundingMode(RoundingMode.FLOOR);
        validators.add(this::validateNoOfVehicles);
        validators.add(this::validateMaxCarriableWt);
        validators.add(this::validateMaxSpeed);
    }


    @Builder
    @AllArgsConstructor
    @Getter
    @ToString
    private static class VehicleAvailability{
        private final short vehicleNo;
        private final double waitTime;
    }


    List<PackageDeliveryInputValidator> validators = new ArrayList<>();
    /**
     *
     * @param courierRequests packages to be delivered
     * @param vehicleInformation captures information like number of available vehicles, max speed, carriable weight
     * @param baseDeliveryCost base delivery cost
     * @return
     */
    @Override
    public List<CourierResponse> calculateEstimatedDelivery(CourierRequest[] courierRequests,
                                                            VehicleInformation vehicleInformation,
                                                            short baseDeliveryCost) {
        assertNotNull(courierRequests, vehicleInformation,baseDeliveryCost);
        validators.forEach( e-> e.validate(vehicleInformation));
        validatePackageWtsAgainstAllowedWts(courierRequests, vehicleInformation);
        PriorityQueue<VehicleAvailability> pq = createPriorityQueue(vehicleInformation);
        initPriorityQueue(vehicleInformation, pq);
        CourierRequest[] copy = Arrays.copyOf(courierRequests, courierRequests.length);
        int len = courierRequests.length;

        List<CourierResponse> courierResponse = new ArrayList<>(len);
        sortPackageDetails(copy); //sort packages by weight and if wait is same sort by distance
        int remainingPackages = len;

        Set<Integer> deliveredPkgIndices = new LinkedHashSet<>(len); //used to hold the visited packages and avoid revisiting
        while (remainingPackages > 0) { //repeat loop until all the packages are delivered
            remainingPackages = calculatePackageDeliveryTimeAndDeliver(vehicleInformation, pq,
                    copy, courierResponse, remainingPackages, deliveredPkgIndices);
        }
        LOGGER.debug("after delivery pq {}",pq);
        pq.clear();
        return arrangeAsInInputOrder(courierRequests, courierResponse);
    }

    /**
     * Validates package weights against max carriable weight
     * @param courierRequests @{@link Package} to be delivered
     * @param vehicleInformation @{@link VehicleInformation} vehicle information
     * @throws InvalidValueException
     */
    private void validatePackageWtsAgainstAllowedWts(CourierRequest[] courierRequests,
                                                     VehicleInformation vehicleInformation) throws InvalidValueException{
        for(CourierRequest courierRequest : courierRequests){
            if(courierRequest.getPackageDetails().getWeight() > vehicleInformation.getMaxCarriableWt()){
                throw new InvalidValueException(String.format(
                        "package weight is %s more than deliverable weight %s",
                        courierRequest.getPackageDetails().getWeight(), vehicleInformation.getMaxCarriableWt()));
            }
        }
    }

    /**
     *
     * @param courierRequests packages to be delivered
     * @param vehicleInformation vehicles used for delivery
     * @param baseDeliveryCost base delivery cost
     */
    private void assertNotNull(CourierRequest[] courierRequests,
                               VehicleInformation vehicleInformation,
                               short baseDeliveryCost){
        assert courierRequests != null : "input packages should not be null";
        assert vehicleInformation != null : "package delivery input should not be null";
        Arrays.stream(courierRequests).forEach(item -> {
            assert item != null : "package should not be null";
        });
        assert baseDeliveryCost >= 0 : "base delivery cost should be between 1 and "+Short.MAX_VALUE;

    }

    /**
     *
     * @param courierRequests input packages
     * @param courierResponse package delivery and cost information
     * @return
     */
    private List<CourierResponse> arrangeAsInInputOrder(CourierRequest[] courierRequests,
                                                        List<CourierResponse>
                                                                courierResponse) {
        Map<String, CourierResponse> result =  courierResponse.stream()
                .collect(Collectors.toMap(CourierResponse::getPackageId, Function.identity()));

        List<CourierResponse> courierResponses = new ArrayList<>(courierRequests.length);
        for(CourierRequest courierRequest : courierRequests){
            courierResponses.add(result.get(
                            courierRequest.getPackageDetails().getPackageId()).toBuilder()
                    .packageId(courierRequest.getPackageDetails().getPackageId()).build());

        }
        return courierResponses;
    }

    private int calculatePackageDeliveryTimeAndDeliver(VehicleInformation vehicleInformation,
                                                       PriorityQueue<VehicleAvailability> pq,
                                                       CourierRequest[] copy,
                                                       List<CourierResponse> courierResponse,
                                                       int remainingPackages, Set<Integer> deliveredPkgIndices) {
        short availableVehicles = vehicleInformation.getNoOfVehicle();
        double currentTripEstimatedDelivery = 0;
        //repeat the loop until vehicles are available and packages are present for delivery
        while(availableVehicles >  0 && remainingPackages > 0) {
            //visited package indices and used to choose which packages to be delivered
            Set<Integer> deliverablePackageIds = deliverPackages(copy, vehicleInformation.getMaxCarriableWt(),
                    deliveredPkgIndices);

            currentTripEstimatedDelivery = getCurrentTripEstimatedDelivery(copy, vehicleInformation.getMaxSpeed(),
                    deliverablePackageIds);
            VehicleAvailability vehicleAvailability = pq.poll();
            courierResponse.addAll(updatePackageDeliveryDeliveryEstimation(copy,
                    vehicleInformation.getMaxSpeed(), deliverablePackageIds,vehicleAvailability));
            LOGGER.debug("currentTripEstimatedDelivery {} ",currentTripEstimatedDelivery);
            deliveredPkgIndices.addAll(deliverablePackageIds);//update delivered packages indices
            remainingPackages -= deliverablePackageIds.size();
            VehicleAvailability vehicleAvailabilityUpdatedWaitTime =
                    VehicleAvailability.builder().vehicleNo(vehicleAvailability.getVehicleNo())
                    .waitTime(vehicleAvailability.getWaitTime() + (currentTripEstimatedDelivery * 2)).build();
            LOGGER.debug("vehicleAvailabilityUpdatedWaitTime {}",vehicleAvailabilityUpdatedWaitTime);
            pq.offer(vehicleAvailabilityUpdatedWaitTime);
            availableVehicles--;
        }
        return remainingPackages;
    }

    /**
     * creates priority queue
     * @param vehicleInformation packages delivery input
     * @return @{@link PriorityQueue}
     */
    private PriorityQueue<VehicleAvailability> createPriorityQueue(VehicleInformation vehicleInformation) {
        return new PriorityQueue<>(vehicleInformation.getNoOfVehicle(),
                (o1, o2) -> (int) (Math.ceil(o1.getWaitTime()) - Math.ceil(o2.getWaitTime())));
    }

    /**
     * initializes priority queue with available vehicles and deliverytime as 0
     * @param vehicleInformation @{@link VehicleInformation}
     * @param pq priority queue
     */
    private void initPriorityQueue(VehicleInformation vehicleInformation, PriorityQueue<VehicleAvailability> pq) {
        for(int i = 1; i <= vehicleInformation.getNoOfVehicle(); i++){
            pq.offer(VehicleAvailability.builder().vehicleNo((short) i).waitTime(0).build());
        }
    }


    /**
     * sorts packages by weight and then distance in ascending order
     * @param courierRequests packages to be delivered
     */
    private  void sortPackageDetails(CourierRequest[] courierRequests) {
        Arrays.sort(courierRequests, 0, courierRequests.length, (o1, o2) -> {
            if(o1.getPackageDetails().getWeight() == o2.getPackageDetails().getWeight()){
                return  o2.getPackageDetails().getDist() - o1.getPackageDetails().getDist();
            }
            return o1.getPackageDetails().getWeight() - o2.getPackageDetails().getWeight();
        });
    }


    /**
     *
     * @param courierRequests packages to be delivered
     * @param maxSpeed max speed delivery vehicle can travel
     * @param deliverablePackageIds the delivered package indices so that wont be re-proecessed
     * @return estimated delivery of selected packages for the vehicle in context
     */
    private double getCurrentTripEstimatedDelivery(CourierRequest[] courierRequests, short maxSpeed,
                                                   Set<Integer> deliverablePackageIds) {
        double currentTripEstimatedDelivery = 0;
        if(!deliverablePackageIds.isEmpty()){
            //calculate current trip delivery
            currentTripEstimatedDelivery = estimatedDelivery(courierRequests, deliverablePackageIds, maxSpeed);
        }
        LOGGER.debug("currentTripEstimatedDelivery {}",currentTripEstimatedDelivery);
        return currentTripEstimatedDelivery;
    }

    /**
     *
     * @param courierRequests packages to be delivered
     * @param maxSpeed max speed vehicle can travel
     * @param deliverablePkgIndices packages planned for delivery in current trip
     * @param vehicleAvailability available vehicle for current @{@link CourierRequest}'s delivery
     * @return list of packages to be delivered in current trip
     */
    private List<CourierResponse>
    updatePackageDeliveryDeliveryEstimation(CourierRequest[] courierRequests, short maxSpeed,
                                            Set<Integer> deliverablePkgIndices,
                                            VehicleAvailability vehicleAvailability) {
        List<CourierResponse> courierResponse = new ArrayList<>(deliverablePkgIndices.size());
        LOGGER.debug("vehicle availability {}",vehicleAvailability);
        for(int index : deliverablePkgIndices){
            double estimatedDeliveryPerPackage = getRoundedValue((vehicleAvailability.getWaitTime()) +
                    ( (double) courierRequests[index].getPackageDetails().getDist() / maxSpeed));
            courierResponse.add(CourierResponse.builder()
                    .estimatedDeliveryTime(estimatedDeliveryPerPackage)
                    .packageId(courierRequests[index].getPackageDetails().getPackageId())
                    .build());

            LOGGER.debug("vehicle {} delivering {} delivery time {}",vehicleAvailability.getVehicleNo(), courierRequests[index],
                    estimatedDeliveryPerPackage);
        }
        return courierResponse;
    }

    /**
     * formats value 0.00
     * @param value to be formatted
     * @return formatted value with 2 decimals
     */
    private double getRoundedValue(double value) {
        return Double.parseDouble(df.format(value));
    }

    /**
     *
     * @param courierRequests packages to be delivered
     * @param maxWt max weight the vehicle allowed to carry
     * @param deliveredPkgIndices package indices that are already delivered
     * @return set of package indices to be delivered
     */
    private Set<Integer> deliverPackages(CourierRequest[] courierRequests, short maxWt, Set<Integer> deliveredPkgIndices) {
        Set<Integer> deliverablePackageIds = new LinkedHashSet<>();
        short prevWt = Short.MIN_VALUE;
        int len = courierRequests.length;
        for (int i = 0; i < len; i++) {
            //package is not delivered
            if (!isPackageDelivered(deliveredPkgIndices, i)) {
                //current package indices that can be planned to package with i th index
                Set<Integer> currIds = new LinkedHashSet<>();
                short currentWt = getCurrentDeliverablePackagesTotalWt(courierRequests, maxWt, deliveredPkgIndices, i, currIds);
                LOGGER.trace("currentIds {}", currIds);
                if (isCurrentAndPrevDelivarablePkgsAreSameSize(deliverablePackageIds, currIds)) { //logic to pick higher weight when there are equal number of packages are present
                    if (currentWt > prevWt) {
                        clearAndAdd(deliverablePackageIds, currIds);
                    }
                } else if (isCurrentPkgsAreGreater(deliverablePackageIds, currIds)) { //preference to deliver higher number of packages
                    clearAndAdd(deliverablePackageIds, currIds);
                }
                if (isNoOrSinglePackagePicked(deliverablePackageIds) && !isAllPackagesDelivered(deliveredPkgIndices, len)) {
                    //corner case, when there is no or only one package is picked
                    //pick the maximum weight package if any available
                    for (int k = len-1; k >= 0; k--) {
                        if (!isPackageDelivered(deliveredPkgIndices, k)) {
                            deliverablePackageIds.add(k);
                            break;
                        }
                    }
                }
            }
        }
        return deliverablePackageIds;
    }


    /**
     *
     * @param deliverablePackageIds deliverable package indices
     * @return true if zero one package is picked in current delivery
     */
    private  boolean isNoOrSinglePackagePicked(Set<Integer> deliverablePackageIds) {
        return deliverablePackageIds.isEmpty() || deliverablePackageIds.size() == 1;
    }

    /**
     *
     * @param deliveredPackageIndices delivered package indices
     * @param totalPackages number of packages to be delivered
     * @return true if all packages not delivered
     */
    private boolean isAllPackagesDelivered(Set<Integer> deliveredPackageIndices, int totalPackages) {
        return deliveredPackageIndices.size() == totalPackages;
    }

    /**
     *
     * @param deliverablePackageIds previously choosen packages for delivery
     * @param currIds currently choosen packages for delivery
     * @return true if current packages are greater
     */
    private boolean isCurrentPkgsAreGreater(Set<Integer> deliverablePackageIds, Set<Integer> currIds) {
        return currIds.size() > deliverablePackageIds.size();
    }

    /**
     *
     * @param deliverablePackageIds previously choosen packages for delivery
     * @param currIds currently choosen packages for delivery
     * @return true if same number packages choosen in prev and current context
     */
    private boolean isCurrentAndPrevDelivarablePkgsAreSameSize(Set<Integer> deliverablePackageIds, Set<Integer> currIds) {
        return currIds.size() == deliverablePackageIds.size();
    }

    /**
     * updates deliverable package Id's
     * @param deliverablePackageIds deliverable package ID's
     * @param currIds current package Ids
     */
    private void clearAndAdd(Set<Integer> deliverablePackageIds, Set<Integer> currIds) {
        deliverablePackageIds.clear();
        deliverablePackageIds.addAll(currIds);
    }

    /**
     *
     * @param courierRequests packages to be delivered
     * @param maxWt max weight the vehicle can carry
     * @param packagesDelivered delivered package indices
     * @param pkgIndex to be included in current trip
     * @param currIds current packages to be delivered
     * @return possible weight for delivery along with @inputPackages[pkgIndex]
     */
    private short getCurrentDeliverablePackagesTotalWt(CourierRequest[] courierRequests, short maxWt,
                                                       Set<Integer> packagesDelivered, int pkgIndex,
                                                       Set<Integer> currIds) {
        int len = courierRequests.length;
        short currentWt =  courierRequests[pkgIndex].getPackageDetails().getWeight();
        //is sum of current weight and jth package less than or equal to max allowable weight
        //then consider jth index with ith index
        for (int j = pkgIndex + 1; j < len && currentWt + courierRequests[j].getPackageDetails().getWeight() <= maxWt; j++) {
            if(!isPackageDelivered(packagesDelivered, j)){
                currentWt += courierRequests[j].getPackageDetails().getWeight();
                currIds.add(pkgIndex);
                currIds.add(j);
                //break the loop as it is not possible to add any more packages
                if(currentWt == maxWt){
                    break;
                }
            }
        }
        return currentWt;
    }

    /**
     *
     * @param delievedPkgs delivered packages
     * @param idx index package
     * @return true if package is delivered
     */
    private boolean isPackageDelivered(Set<Integer> delievedPkgs, int idx) {
        return delievedPkgs.contains(idx);
    }

    /**
     *
     * @param courierRequests packages planned for delivery
     * @param deliveryIds packages choosen for delivery
     * @param maxSpeed max speed vehicle can travel
     * @return estimated delivery
     */
    private double estimatedDelivery(CourierRequest[] courierRequests, Set<Integer> deliveryIds, short maxSpeed){

        double maxDist = Short.MIN_VALUE;

        for(int deliveryId : deliveryIds){
            if(courierRequests[deliveryId].getPackageDetails().getDist() > maxDist){
                maxDist = courierRequests[deliveryId].getPackageDetails().getDist();
            }
        }
        return getRoundedValue((maxDist / maxSpeed)) ;
    }
    private interface PackageDeliveryInputValidator{
        boolean validate(VehicleInformation vehicleInformation) throws InvalidValueException;
    }

    private boolean validateMaxCarriableWt(VehicleInformation vehicleInformation){
        if(!vehicleInformation.isValidMaxCarriableWt()){
            throw new InvalidValueException("invalid value for max carriable weight "+ vehicleInformation.getMaxCarriableWt());
        }
        return true;
    }

    private boolean validateNoOfVehicles(VehicleInformation vehicleInformation){
        if(!vehicleInformation.isNoOfVehicleValid()){
            throw new InvalidValueException("invalid value for number of vehicles "+ vehicleInformation.getNoOfVehicle());
        }
        return true;
    }

    private boolean validateMaxSpeed(VehicleInformation vehicleInformation){
        if(!vehicleInformation.isValidMaxSpeed()){
            throw new InvalidValueException("invalid value for max speed "+ vehicleInformation.getMaxSpeed());
        }
        return true;
    }

}
