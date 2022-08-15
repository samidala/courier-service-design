package com.everestengg.code.challenge.service.delivery.time.estimation;

import com.everestengg.code.challenge.exceptions.InvalidValueException;
import com.everestengg.code.challenge.model.courier.PackageDeliveryCostAndTimeEstimationInfo;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.PackageDeliveryInput;
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
import java.util.PriorityQueue;
import java.util.Set;

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
     * @param inputPackages packages to be delivered
     * @param packageDeliveryInput captures information like number of available vehicles, max speed, carriable weight
     * @param baseDeliveryCost base delivery cost
     * @return
     */
    @Override
    public List<PackageDeliveryCostAndTimeEstimationInfo> calculateEstimatedDelivery(InputPackage[] inputPackages,
                                                                                     PackageDeliveryInput packageDeliveryInput,
                                                                                     short baseDeliveryCost) {

        validators.forEach( e-> e.validate(packageDeliveryInput));
        PriorityQueue<VehicleAvailability> pq = createPriorityQueue(packageDeliveryInput);
        initPriorityQueue(packageDeliveryInput, pq);
        InputPackage[] copy = Arrays.copyOf(inputPackages,inputPackages.length);
        int len = inputPackages.length;

        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfo = new ArrayList<>(len);
        sortPackageDetails(copy); //sort packages by weight and if wait is same sort by distance
        int remainingPackages = len;

        Set<Integer> deliveredPkgIndices = new LinkedHashSet<>(len); //used to hold the visited packages and avoid revisiting
        while (remainingPackages > 0) { //repeat loop until all the packages are delivered
            remainingPackages = calculatePackageDeliveryTimeAndDeliver(packageDeliveryInput, pq,
                    copy, packageDeliveryCostAndTimeEstimationInfo, remainingPackages, deliveredPkgIndices);
        }
        LOGGER.debug("after delivery pq {}",pq);
        pq.clear();
        return packageDeliveryCostAndTimeEstimationInfo;
    }

    private int calculatePackageDeliveryTimeAndDeliver(PackageDeliveryInput packageDeliveryInput,
                                                       PriorityQueue<VehicleAvailability> pq,
                                                       InputPackage[] copy,
                                                       List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfo,
                                                       int remainingPackages, Set<Integer> deliveredPkgIndices) {
        short availableVehicles = packageDeliveryInput.getNoOfVehicle();
        double currentTripEstimatedDelivery = 0;
        //repeat the loop until vehicles are available and packages are present for delivery
        while(availableVehicles >  0 && remainingPackages > 0) {
            //visited package indices and used to choose which packages to be delivered
            Set<Integer> deliverablePackageIds = deliverPackages(copy, packageDeliveryInput.getMaxCarriableWt(),
                    deliveredPkgIndices);

            currentTripEstimatedDelivery = getCurrentTripEstimatedDelivery(copy, packageDeliveryInput.getMaxSpeed(),
                    deliverablePackageIds);
            VehicleAvailability vehicleAvailability = pq.poll();
            packageDeliveryCostAndTimeEstimationInfo.addAll(updatePackageDeliveryDeliveryEstimation(copy,
                    packageDeliveryInput.getMaxSpeed(), deliverablePackageIds,vehicleAvailability));
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
     * @param packageDeliveryInput packages delivery input
     * @return @{@link PriorityQueue}
     */
    private PriorityQueue<VehicleAvailability> createPriorityQueue(PackageDeliveryInput packageDeliveryInput) {
        return new PriorityQueue<>(packageDeliveryInput.getNoOfVehicle(),
                (o1, o2) -> (int) (Math.ceil(o1.getWaitTime()) - Math.ceil(o2.getWaitTime())));
    }

    /**
     * initializes priority queue with available vehicles and deliverytime as 0
     * @param packageDeliveryInput @{@link PackageDeliveryInput}
     * @param pq priority queue
     */
    private void initPriorityQueue(PackageDeliveryInput packageDeliveryInput, PriorityQueue<VehicleAvailability> pq) {
        for(int i = 1; i <= packageDeliveryInput.getNoOfVehicle(); i++){
            pq.offer(VehicleAvailability.builder().vehicleNo((short) i).waitTime(0).build());
        }
    }


    /**
     * sorts packages by weight and then distance in ascending order
     * @param inputPackages packages to be delivered
     */
    private  void sortPackageDetails(InputPackage[] inputPackages) {
        Arrays.sort(inputPackages, 0, inputPackages.length, (o1, o2) -> {
            if(o1.getPackageDetails().getWeight() == o2.getPackageDetails().getWeight()){
                return  o2.getPackageDetails().getDist() - o1.getPackageDetails().getDist();
            }
            return o1.getPackageDetails().getWeight() - o2.getPackageDetails().getWeight();
        });
    }


    /**
     *
     * @param inputPackages packages to be delivered
     * @param maxSpeed max speed delivery vehicle can travel
     * @param deliverablePackageIds the delivered package indices so that wont be re-proecessed
     * @return estimated delivery of selected packages for the vehicle in context
     */
    private double getCurrentTripEstimatedDelivery(InputPackage[] inputPackages, short maxSpeed,
                                                         Set<Integer> deliverablePackageIds) {
        double currentTripEstimatedDelivery = 0;
        if(!deliverablePackageIds.isEmpty()){
            //calculate current trip delivery
            currentTripEstimatedDelivery = estimatedDelivery(inputPackages, deliverablePackageIds, maxSpeed);
        }
        LOGGER.debug("currentTripEstimatedDelivery {}",currentTripEstimatedDelivery);
        return currentTripEstimatedDelivery;
    }

    /**
     *
     * @param inputPackages packages to be delivered
     * @param maxSpeed max speed vehicle can travel
     * @param deliverablePkgIndices packages planned for delivery in current trip
     * @param vehicleAvailability available vehicle for current @{@link InputPackage}'s delivery
     * @return list of packages to be delivered in current trip
     */
    private List<PackageDeliveryCostAndTimeEstimationInfo>
    updatePackageDeliveryDeliveryEstimation(InputPackage[] inputPackages, short maxSpeed,
                                            Set<Integer> deliverablePkgIndices,
                                            VehicleAvailability vehicleAvailability) {
        List<PackageDeliveryCostAndTimeEstimationInfo> packageDeliveryCostAndTimeEstimationInfo = new ArrayList<>(deliverablePkgIndices.size());
        LOGGER.debug("vehicle availability {}",vehicleAvailability);
        for(int index : deliverablePkgIndices){
            double estimatedDeliveryPerPackage = getRoundedValue((vehicleAvailability.getWaitTime()) +
                    ( (double) inputPackages[index].getPackageDetails().getDist() / maxSpeed));
            packageDeliveryCostAndTimeEstimationInfo.add(PackageDeliveryCostAndTimeEstimationInfo.builder()
                    .estimatedDeliveryTime(estimatedDeliveryPerPackage)
                    .packageId(inputPackages[index].getPackageDetails().getPackageId())
                    .build());

            LOGGER.debug("vehicle {} delivering {} delivery time {}",vehicleAvailability.getVehicleNo(),inputPackages[index],
                    estimatedDeliveryPerPackage);
        }
        return packageDeliveryCostAndTimeEstimationInfo;
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
     * @param inputPackages packages to be delivered
     * @param maxWt max weight the vehicle allowed to carry
     * @param deliveredPkgIndices package indices that are already delivered
     * @return set of package indices to be delivered
     */
    private Set<Integer> deliverPackages(InputPackage[] inputPackages, short maxWt, Set<Integer> deliveredPkgIndices) {
        Set<Integer> deliverablePackageIds = new LinkedHashSet<>();
        short prevWt = Short.MIN_VALUE;
        int len = inputPackages.length;
        for (int i = 0; i < len; i++) {
            //package is not delivered
            if (!isPackageDelivered(deliveredPkgIndices, i)) {
                //current package indices that can be planned to package with i th index
                Set<Integer> currIds = new LinkedHashSet<>();
                short currentWt = getCurrentDeliverablePackagesTotalWt(inputPackages, maxWt, deliveredPkgIndices, i, currIds);
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
     * @param inputPackages packages to be delivered
     * @param maxWt max weight the vehicle can carry
     * @param packagesDelivered delivered package indices
     * @param pkgIndex to be included in current trip
     * @param currIds current packages to be delivered
     * @return possible weight for delivery along with @inputPackages[pkgIndex]
     */
    private short getCurrentDeliverablePackagesTotalWt(InputPackage[] inputPackages, short maxWt,
                                                              Set<Integer> packagesDelivered, int pkgIndex,
                                                              Set<Integer> currIds) {
        int len = inputPackages.length;
        short currentWt =  inputPackages[pkgIndex].getPackageDetails().getWeight();
        //is sum of current weight and jth package less than or equal to max allowable weight
        //then consider jth index with ith index
        for (int j = pkgIndex + 1; j < len && currentWt + inputPackages[j].getPackageDetails().getWeight() <= maxWt; j++) {
            if(!isPackageDelivered(packagesDelivered, j)){
                currentWt += inputPackages[j].getPackageDetails().getWeight();
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
     * @param inputPackages packages planned for delivery
     * @param deliveryIds packages choosen for delivery
     * @param maxSpeed max speed vehicle can travel
     * @return estimated delivery
     */
    private double estimatedDelivery(InputPackage[] inputPackages, Set<Integer> deliveryIds, short maxSpeed){

        double maxDist = Short.MIN_VALUE;

        for(int deliveryId : deliveryIds){
            if(inputPackages[deliveryId].getPackageDetails().getDist() > maxDist){
                maxDist = inputPackages[deliveryId].getPackageDetails().getDist();
            }
        }
        return getRoundedValue((maxDist / maxSpeed)) ;
    }
    private interface PackageDeliveryInputValidator{
        boolean validate(PackageDeliveryInput packageDeliveryInput) throws InvalidValueException;
    }

    private boolean validateMaxCarriableWt(PackageDeliveryInput packageDeliveryInput){
        if(!packageDeliveryInput.isValidMaxCarriableWt()){
            throw new InvalidValueException("invalid value for max carriable weight "+packageDeliveryInput.getMaxCarriableWt());
        }
        return true;
    }

    private boolean validateNoOfVehicles(PackageDeliveryInput packageDeliveryInput){
        if(!packageDeliveryInput.isNoOfVehicleValid()){
            throw new InvalidValueException("invalid value for number of vehicles "+packageDeliveryInput.getNoOfVehicle());
        }
        return true;
    }

    private boolean validateMaxSpeed(PackageDeliveryInput packageDeliveryInput){
        if(!packageDeliveryInput.isValidMaxSpeed()){
            throw new InvalidValueException("invalid value for max speed "+packageDeliveryInput.getMaxSpeed());
        }
        return true;
    }

}
