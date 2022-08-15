package com.everestengg.code.challenge.service.delivery.cost.estimation;

import com.everestengg.code.challenge.exceptions.InvalidValueException;
import com.everestengg.code.challenge.model.courier.PackageDeliveryCostEstimateInfo;
import com.everestengg.code.challenge.service.offer.OfferServiceImpl;
import com.everestengg.code.challenge.vo.InputPackage;
import com.everestengg.code.challenge.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * implementation of @{@link PackageDeliveryCostEstimationService}
 */
public class PackageDeliveryCostEstimationImpl implements PackageDeliveryCostEstimationService {

    public static final int WEIGHT_MULTIPLIER = 10;
    public static final int DISTANCE_MULTIPLIER = 5;

    private final int weightMultiplier;
    private final int distanceMultiplier;
    List<InputPackageValidator> inputPackageValidatorList = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageDeliveryCostEstimationImpl.class);

    public PackageDeliveryCostEstimationImpl(){
        this.weightMultiplier = WEIGHT_MULTIPLIER;
        this.distanceMultiplier = DISTANCE_MULTIPLIER;
        this.inputPackageValidatorList.add(this::isValidPackageId);
        this.inputPackageValidatorList.add(this::isValidDistance);
        this.inputPackageValidatorList.add(this::isValidWeight);
    }

    public PackageDeliveryCostEstimationImpl(int weightMultiplier, int distanceMultiplier) {
        this.weightMultiplier = weightMultiplier;
        this.distanceMultiplier = distanceMultiplier;
    }

    @Override
    public List<InputPackageValidator> getInputPackageValidatorList() {
        return Collections.unmodifiableList(inputPackageValidatorList);
    }

    /**
     *
     * @param inputPackage packages to be delivered
     * @param baseDeliveryCost base delivery cost
     * @return PackageChargeInformation
     */
    public Response<PackageDeliveryCostEstimateInfo> calcCost(InputPackage inputPackage, float baseDeliveryCost){
        isValidBaseDeliveryCost(baseDeliveryCost);
        inputPackageValidatorList.forEach(e-> e.validate(inputPackage));
        isValidOfferCode(inputPackage);
        float discountValueInPercentage = new OfferServiceImpl().getDiscountValue(inputPackage)/100;
        float totalCost = baseDeliveryCost + (inputPackage.getPackageDetails().getWeight() * getWeightMultiplier())
                + (inputPackage.getPackageDetails().getDist() * getDistanceMultiplier()) ;
        LOGGER.trace("totalCost {}",totalCost);
        LOGGER.trace("discountValueInPercentage {}",discountValueInPercentage);
        float totalDiscount = (totalCost *discountValueInPercentage);
        LOGGER.trace("totalDiscount {}",totalDiscount);
        float costAfterDiscount = totalCost - totalDiscount;
        LOGGER.trace("costAfterDiscount {}",costAfterDiscount);
        return Response.<PackageDeliveryCostEstimateInfo>builder()
                .errorCode(null)
                .result(PackageDeliveryCostEstimateInfo.builder().packageId(inputPackage
                                .getPackageDetails().getPackageId())
                        .totalCost(totalCost)
                        .totalDiscount(totalDiscount).costAfterDiscount(costAfterDiscount).build())
                .build();
    }


    public interface InputPackageValidator{
        boolean validate(InputPackage inputPackage) throws InvalidValueException;
    }

    private  boolean isValidPackageId(InputPackage inputPackage) {
        if(!inputPackage.getPackageDetails().isValidPackageId()){
            LOGGER.warn("invalid package ID {}",inputPackage.getPackageDetails().getPackageId());
            throw new InvalidValueException("invalid package ID "+inputPackage.getPackageDetails().getPackageId());
        }
        return true;
    }
    private  boolean isValidBaseDeliveryCost(float baseDeliveryCost ) {
        if(baseDeliveryCost < 0){
            LOGGER.warn("invalid base delivery cost {}",baseDeliveryCost);
            throw new InvalidValueException("invalid baseDeliveryCost "+baseDeliveryCost);
        }
        return true;
    }
    private  boolean isValidDistance(InputPackage inputPackage){
        if(!inputPackage.getPackageDetails().isValidDistance()){
            LOGGER.warn("invalid distance {}",inputPackage.getPackageDetails().getDist());
            throw new InvalidValueException("invalid distance "+inputPackage.getPackageDetails().getDist());
        }
        return true;
    }
    private  boolean isValidWeight(InputPackage inputPackage){
        if(!inputPackage.getPackageDetails().isValidWeight()){
            LOGGER.warn("invalid weight {}",inputPackage.getPackageDetails().getWeight());
            throw new InvalidValueException("invalid weight "+inputPackage.getPackageDetails().getWeight());
        }
        return true;
    }

    private void isValidOfferCode(InputPackage inputPackage){
        if(!inputPackage.isValidOfferCode()){
            LOGGER.warn("Invalid offer code, continuing with no discount.. code {}", inputPackage.getOfferCode());
        }
    }
    public int getDistanceMultiplier() {
        return distanceMultiplier;
    }

    public int getWeightMultiplier() {
        return weightMultiplier;
    }
}
