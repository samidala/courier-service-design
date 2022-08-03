package com.everestengg.code.challenge.model;


public class StringOfferCriteria extends OfferCriteria<String,String>{

    public StringOfferCriteria(Operator operator, String propertyValue, ValueHandler<String> valueHandler) {
        super(operator, propertyValue,valueHandler);
        init();
    }

    @Override
    public boolean isMatch(String value) {
        return getMap().get(getOperator()).evaluate(this,value);
    }

    private  abstract class AbstractResultEvaluator implements ResultEvaluator<String,String>{
        public AbstractResultEvaluator() {

        }
        @Override
        public void register(OfferCriteria<String,String> offerCriteria) {
            getMap().put(offerCriteria.getOperator(),this);
        }
    }



    private final ResultEvaluator<String,String> EQUAL_EVALUATOR = new AbstractResultEvaluator() {
        @Override
        public boolean evaluate(OfferCriteria<String,String> offerCriteria, String value) {
            return offerCriteria.getPropertyValue().equals(value);
        }

    };


    public void init()
    {
        EQUAL_EVALUATOR.register(this);
    }

}
