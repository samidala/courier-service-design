package com.everestengg.code.challenge.model;

public class NumberRangeOfferCriteria extends OfferCriteria<Number[],Number>{

    public NumberRangeOfferCriteria(Operator operator, Number[] propertyValue,ValueHandler valueHandler) {
        super( operator, propertyValue,valueHandler);
        init();
    }

    @Override
    public boolean isMatch(Number value) {
        return getMap().getOrDefault(this.getOperator(),
                RANGE_EVALUATOR).evaluate(this,value);
    }



    private  abstract class AbstractResultEvaluator implements ResultEvaluator<Number[],Number>{
        @Override
        public void register(OfferCriteria<Number[],Number> offerCriteria) {
            getMap().put(offerCriteria.getOperator(),this);
        }
    }
    private  ResultEvaluator<Number[],Number> RANGE_EVALUATOR = new AbstractResultEvaluator() {
        @Override
        public boolean evaluate(OfferCriteria<Number[],Number> offerCriteria, Number value) {

            return value.doubleValue() >= offerCriteria.getPropertyValue()[0].doubleValue() &&
                    value.doubleValue() <= offerCriteria.getPropertyValue()[1].doubleValue();
        }
    };
    public void init()
    {
        RANGE_EVALUATOR.register(this);
    }



}
