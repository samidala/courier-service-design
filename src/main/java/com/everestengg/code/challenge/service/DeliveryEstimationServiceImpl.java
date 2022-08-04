package com.everestengg.code.challenge.service;

import com.everestengg.code.challenge.bo.DeliveryInput;
import com.everestengg.code.challenge.bo.InputPackage;
import com.everestengg.code.challenge.model.DeliveryOrder;
import com.everestengg.code.challenge.model.PackageChargeInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.everestengg.code.challenge.service.PackageOrderFactory.PackageServiceType.SIMPLE;

public class DeliveryEstimationServiceImpl implements DeliveryEstimationService{

    private static Logger LOGGER = LoggerFactory.getLogger(DeliveryEstimationServiceImpl.class);

    /**
     *
     * @param inputPackages packages to be delivered
     * @param deliveryInput captures information like number of available vehicles, max speed, carriable weight
     * @param baseDeliveryCost base delivery cost
     * @return
     */
    @Override
    public List<DeliveryOrder> calculateEstimatedDelivery(InputPackage[] inputPackages,
                                                          DeliveryInput deliveryInput, short baseDeliveryCost) {
        List<DeliveryOrder> deliveryOrder = new ArrayList<>(inputPackages.length);
        sortPackageDetails(inputPackages); //sort packages by weight and if wait is same sort by distance
        int remainingPackages = inputPackages.length;
        Set<Integer> deliveredPkgIndices = new LinkedHashSet<>(inputPackages.length); //used to hold the visited packages and avoid revisiting
        float minEstimatedDelivery = Float.MAX_VALUE;
        while (remainingPackages > 0) { //repeat loop until all the packages are delivered
            short availableVehicles = 1;
            float currentTripEstimatedDelivery = 0;
            //repeat the loop until vehicles are available and packages are present for delivery
            while(availableVehicles <=  deliveryInput.getNoOfVehicle() && remainingPackages > 0) {
                //visited package indices and used to choose which packages to be delivered
                Set<Integer> deliverablePackageIds = deliverPackages(inputPackages, deliveryInput.getMaxCarriableWt(),
                        deliveredPkgIndices);

                currentTripEstimatedDelivery = getCurrentTripEstimatedDelivery(inputPackages, deliveryInput.getMaxSpeed(),
                        deliverablePackageIds);
                float waitTime = (minEstimatedDelivery == Float.MAX_VALUE) ? 0 : minEstimatedDelivery * 2;
                deliveryOrder.addAll(getDeliveryPackageOrder(inputPackages, deliveryInput.getMaxSpeed(), availableVehicles,
                        deliverablePackageIds, waitTime,baseDeliveryCost));
                deliveredPkgIndices.addAll(deliverablePackageIds);//update delivered packages indices
                remainingPackages -= deliverablePackageIds.size();
                availableVehicles++;
            }
            minEstimatedDelivery = Math.min(currentTripEstimatedDelivery,minEstimatedDelivery);
        }
        return deliveryOrder;
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
    private float getCurrentTripEstimatedDelivery(InputPackage[] inputPackages, short maxSpeed,
                                                         Set<Integer> deliverablePackageIds) {
        float currentTripEstimatedDelivery = 0;
        if(!deliverablePackageIds.isEmpty()){
            //calculate current trip delivery
            currentTripEstimatedDelivery = estimatedDelivery(inputPackages, deliverablePackageIds, maxSpeed);
        }
        return currentTripEstimatedDelivery;
    }

    /**
     *
     * @param inputPackages packages to be delivered
     * @param maxSpeed max speed vehicle can travel
     * @param availableVehicle avaialble vehicle in context for delivery
     * @param deliverablePkgIndices packages planned for delivery in current trip
     * @param waitTime wait to be added if vehicle is not available
     * @param baseDeliveryCost what is the base delivery cost
     * @return list of packages to be delivered in current trip
     */
    private static List<DeliveryOrder> getDeliveryPackageOrder(InputPackage[] inputPackages, short maxSpeed, short availableVehicle,
                                                               Set<Integer> deliverablePkgIndices, float waitTime,
                                                               short baseDeliveryCost) {
        List<DeliveryOrder> deliveryOrder = new ArrayList<>(deliverablePkgIndices.size());
        for(int index : deliverablePkgIndices){
            float estimatedDelivery = (waitTime + (((float) inputPackages[index].getPackageDetails().getDist()) / maxSpeed));
            PackageChargeInformation packageChargeInformation = PackageOrderFactory.getPackageOrderService(SIMPLE)
                    .calcCost(inputPackages[index], baseDeliveryCost);
            deliveryOrder.add(DeliveryOrder.builder().packageChargeInformation(packageChargeInformation)
                    .estimatedDeliveryTime(estimatedDelivery).build());
            LOGGER.debug("vehicle {} delivering {} delivery time {}",availableVehicle,inputPackages[index],
                    estimatedDelivery);
          //  System.out.println("waitTime "+waitTime);
           // System.out.println(availableVehicle + " delivering "+ inputPackages[index]);
        }
        return deliveryOrder;
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
    private float estimatedDelivery(InputPackage[] inputPackages, Set<Integer> deliveryIds, short maxSpeed){

        short maxDist = Short.MIN_VALUE;

        for(int deliveryId : deliveryIds){
            if(inputPackages[deliveryId].getPackageDetails().getDist() > maxDist){
                maxDist = inputPackages[deliveryId].getPackageDetails().getDist();
            }
        }
        return ((float)maxDist) / maxSpeed ;
    }


}
