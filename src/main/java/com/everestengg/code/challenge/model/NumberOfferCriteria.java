package com.everestengg.code.challenge.model;


/**
 * implementation for configuring Number and value also a Number for offer criteria
 */
public class NumberOfferCriteria extends OfferCriteria<Number,Number>{

    public NumberOfferCriteria(Operator operator, Number propertyValue,ValueHandler valueHandler) {
        super( operator, propertyValue,valueHandler);
        init();
    }



    @Override
    public boolean isMatch(Number value) {
        return getMap().get(getOperator()).evaluate(this,value);
    }

    private  abstract class AbstractResultEvaluator implements ResultEvaluator<Number,Number>{
        public AbstractResultEvaluator() {

        }
        @Override
        public void register(OfferCriteria<Number,Number> offerCriteria) {
            getMap().put(offerCriteria.getOperator(),this);
        }
    }



    private final ResultEvaluator<Number,Number> EQUAL_EVALUATOR = new AbstractResultEvaluator() {
        @Override
        public boolean evaluate(OfferCriteria<Number,Number> offerCriteria, Number value) {
            return offerCriteria.getPropertyValue().doubleValue() == value.doubleValue();
        }

    };

    private final ResultEvaluator<Number,Number> NOT_EQUAL_EVALUATOR = new AbstractResultEvaluator() {
        @Override
        public boolean evaluate(OfferCriteria<Number,Number> offerCriteria, Number value) {
            return offerCriteria.getPropertyValue().doubleValue() != value.doubleValue();
        }
    };

    private final ResultEvaluator<Number,Number> GT_EVALUATOR = new AbstractResultEvaluator() {
        @Override
        public boolean evaluate(OfferCriteria<Number,Number> offerCriteria, Number value) {
            return value.doubleValue() > offerCriteria.getPropertyValue().doubleValue();
        }
    };

    private final ResultEvaluator<Number,Number> LT_EVALUATOR = new AbstractResultEvaluator() {
        @Override
        public boolean evaluate(OfferCriteria<Number,Number> offerCriteria, Number value) {
            return  value.doubleValue() < offerCriteria.getPropertyValue().doubleValue();
        }
    };

    private final ResultEvaluator<Number,Number> LTE_EVALUATOR = new AbstractResultEvaluator() {
        @Override
        public boolean evaluate(OfferCriteria<Number,Number> offerCriteria, Number value) {
            return value.doubleValue() <= offerCriteria.getPropertyValue().doubleValue() ;
        }
    };

    private final ResultEvaluator<Number,Number> GTE_EVALUATOR = new AbstractResultEvaluator() {
        @Override
        public boolean evaluate(OfferCriteria<Number,Number> offerCriteria, Number value) {
            return value.doubleValue() >= offerCriteria.getPropertyValue().doubleValue() ;
        }
    };

    public void init()
    {
        EQUAL_EVALUATOR.register(this);
        NOT_EQUAL_EVALUATOR.register(this);
        GT_EVALUATOR.register(this);
        LT_EVALUATOR.register(this);
        GTE_EVALUATOR.register(this);
        LTE_EVALUATOR.register(this);
    }

}
