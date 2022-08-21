package com.techdisqus.courier.service.delivery.cost.estimation;

import com.techdisqus.courier.exceptions.InvalidValueException;
import com.techdisqus.courier.vo.courier.PackageDeliveryCostEstimateInfo;
import com.techdisqus.courier.service.offer.OfferServiceImpl;
import com.techdisqus.courier.vo.courier.CourierRequest;
import com.techdisqus.courier.vo.Response;
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
     * @param courierRequest packages to be delivered
     * @param baseDeliveryCost base delivery cost
     * @return PackageChargeInformation
     */
    public Response<PackageDeliveryCostEstimateInfo> calcCost(CourierRequest courierRequest, float baseDeliveryCost){
        assertNotNull(courierRequest);
        isValidBaseDeliveryCost(baseDeliveryCost);
        inputPackageValidatorList.forEach(e-> e.validate(courierRequest));
        isValidOfferCode(courierRequest);
        float discountValueInPercentage = new OfferServiceImpl().getDiscountValue(courierRequest)/100;
        float totalCost = baseDeliveryCost + (courierRequest.getPackageDetails().getWeight() * getWeightMultiplier())
                + (courierRequest.getPackageDetails().getDist() * getDistanceMultiplier()) ;
        LOGGER.trace("totalCost {}",totalCost);
        LOGGER.trace("discountValueInPercentage {}",discountValueInPercentage);
        float totalDiscount = (totalCost *discountValueInPercentage);
        LOGGER.trace("totalDiscount {}",totalDiscount);
        float costAfterDiscount = totalCost - totalDiscount;
        LOGGER.trace("costAfterDiscount {}",costAfterDiscount);
        return Response.<PackageDeliveryCostEstimateInfo>builder()
                .errorCode(null)
                .result(PackageDeliveryCostEstimateInfo.builder().packageId(courierRequest
                                .getPackageDetails().getPackageId())
                        .totalCost(totalCost)
                        .totalDiscount(totalDiscount).costAfterDiscount(costAfterDiscount).build())
                .build();
    }

    private void assertNotNull(CourierRequest courierRequest) {
        assert courierRequest != null : "input package should not be null";
        assert courierRequest.getPackageDetails() != null : "package should not be null";
    }


    public interface InputPackageValidator{
        boolean validate(CourierRequest courierRequest) throws InvalidValueException;
    }

    private  boolean isValidPackageId(CourierRequest courierRequest) {
        if(!courierRequest.getPackageDetails().isValidPackageId()){
            LOGGER.warn("invalid package ID {}", courierRequest.getPackageDetails().getPackageId());
            throw new InvalidValueException("invalid package ID "+ courierRequest.getPackageDetails().getPackageId());
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
    private  boolean isValidDistance(CourierRequest courierRequest){
        if(!courierRequest.getPackageDetails().isValidDistance()){
            LOGGER.warn("invalid distance {}", courierRequest.getPackageDetails().getDist());
            throw new InvalidValueException("invalid distance "+ courierRequest.getPackageDetails().getDist());
        }
        return true;
    }
    private  boolean isValidWeight(CourierRequest courierRequest){
        if(!courierRequest.getPackageDetails().isValidWeight()){
            LOGGER.warn("invalid weight {}", courierRequest.getPackageDetails().getWeight());
            throw new InvalidValueException("invalid weight "+ courierRequest.getPackageDetails().getWeight());
        }
        return true;
    }

    private void isValidOfferCode(CourierRequest courierRequest){
        if(!courierRequest.isValidOfferCode()){
            LOGGER.warn("Invalid offer code, continuing with no discount.. code {}", courierRequest.getOfferCode());
        }
    }
    public int getDistanceMultiplier() {
        return distanceMultiplier;
    }

    public int getWeightMultiplier() {
        return weightMultiplier;
    }
}
