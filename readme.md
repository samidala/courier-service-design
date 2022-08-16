
# Setting up code base

1. Have java 8 installed on machine.
2. Have IntelliJ installed.
3. Have maven installed.
4. Enable annotation processing. File menu --> Settings --> Build, execution, deployment --> compile --> annotation process and check  enable annotation processing
   ![img.png](img.png)
5. Wait for maven project to load, if not automatically done, right click on pom.xml and add as maven project.

# Delivery cost client
# Assumptions: 

1. The agenda of the project is concentrate on design aspect and write code that can be extendable to different offer codes
2. If offer code is invalid, no discount will be considered
3. User has to input all the needed inputs in appropriate datatype else application would show error and exit.

# Setting up offers

# approach
   1. `Offer` will have 0 or more `OfferCriteria`
   2. User can configure offer with code and set of `OfferCriteria`
   3. `OfferCriteria`'s `isMatch` responsible for validating if offer criteria is met
   4. `com.everestengg.code.challenge.model.offer.Offer.calcDiscount` returns configured discount value if all the criteria matches
   5. `com.everestengg.code.challenge.domain.offer.OfferCriteria.ValueHandler` is used read value from target entity.
   6. The below show sequence diagram
      
      ![PackageDeliveryCostEstimationImpl_calcCost.svg](PackageDeliveryCostEstimationImpl_calcCost.svg)


Assumptions
   1. The offer criteria can be expressed in equals, greater or lesser than to some value
   2. The offer criteria can be string for example, specific category
   3. The offer is expressed with multiple offer criteria's and must match to get the discount.
   4. No discount is provided for invalid offer codes.

Go to Utils and create new offer. For example below setup offer with code `OFR003` and criteria as 
distance should range between `50` to `250`, and weight range between `10` to `150`. create a new method to introduce new offer and call the method inside `prepareOffer`

    private static void prepareOffer3() {
        NumberRangeOfferCriteria dist50To250 = new NumberRangeOfferCriteria( RANGE,
        new Number[]{50, 250}, Package::getDist);
        NumberRangeOfferCriteria weight10To150 = new NumberRangeOfferCriteria(RANGE,
        new Number[]{10,150}, Package::getWeight);
        new Offer<Number,Number>("OFR003",5,new OfferCriteria[]{dist50To250,weight10To150});
    }

# Running
1. Run PackageChargeCalculatorApp class

2. Provide base delivery cost and number of packages

3. enter the package details with space separated and press enter after inputting package ID, weight, distance and offer code

4. After successful run, the app displays each package id, discount and charges to be paid.

# Package delivery time estimation

# Assumptions:

1. Considered only positive inputs and no negative cases handled for inputs and hence the app will fail if any wrong inputs provided
2. Concentrated on having a feasible solution, I believe the code can be improvised though I did my best to write understandable code.
3. If offer code is invalid, no discount will be considered
4. User has to input all the needed inputs in appropriate datatype

# Approach
1. Sort the packages by weight in ascending and distance in descending order
2. Build the PriorityQueue with available vehicles and waitTime as 0
3. Repeat below step until all the packages are delivered
   1. Repeat until vehicles are available
      1. pick the packages to be delivered based on below criteria
         1. shipment should contain max number of packages
         2. heavier packages takes precedence if same number of packages in the current trip
         3. if weights also same then pick the package which are can be delivered fast
      2. Calculate estimated delivery of current trip
      3. pool the vehicle from priority queue
   2. Add the vehicle back to the priority queue with updated wait time with current trip time * 2 and current wait time 


# Running

1. Run DeliveryEstimationApp class
2. Provide base delivery cost and number of packages
3. enter the package details with space separated and press enter after inputting package ID, weight, distance and offer code
4. enter no of vehicles, max speed and max carriable weight
5. After successful run, the app displays each package id, discount, charges to be paid and estimated delivery time.
6. Enable the debug logs by changing in `log4j2.xml` for to print debug logs.


# Other ways to run

1. Run DeliveryEstimationServiceTest for delivery time estimation and this printing only the results as above and doesn't have assertions in place as of now however, I shall add if time permits. Sorted the results by package ID's for easy to read the output
2. Run PackageOrderImplTest for calculating the charges for each offer code.
3. `PackageOrderImplTest.testCalcDiscountCategoryAndWeight` simulates support new offer code by category and weight.


# Setting up offers from CSV file

1. Setting up offers
   1. It has the following properties
      1. offerId
      2. discountPercentage
      3. offerCriteriaIds, The offer criteria's are separated with `|`
      4. Example: `OFR001,10,1|2` Here, OFR001 is offer code, 10 is discount percentage and `1|2` are offer criteria ID's
2. Setting up offer criteria used in `1.3` above
   1. It has the following properties
      1. offerCriteriaId, used in above `1.3` point
      2. property
      3. value
   2. value are separated with `|`
   3. example: 1,dist,0|200, here `1` is offer criteria ID, `dist` is the `property`, `0|200` is value range for the offer criteria
   4. There should be value handler (`ValueHandler`) defined for each property get value from package.
   
# improvements:
1. Method naming conventions can be made better.
2. Add Junits for CSV reading


   ![uml.png](uml.png)







