package com.everestengg.code.challenge.model;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * captures offer criteria for configured value and input value
 *
 * @param <ConfigValue> value configured in offer criteria. config value can be single value, array or range
 * @param <InputValue> value coming from package
 */
@Getter
public abstract class OfferCriteria<ConfigValue, InputValue> {

    public enum Operator{
        EQ("=="),
        NEQ("!="),
        GT("GT"),
        GTE("GTE"),
        LT("LT"),
        LTE("LTE"),
        RANGE("RANGE");

        private final String operator;

        Operator(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }
    }
    private final Operator operator;

    private final ConfigValue propertyValue;
    private final Map<Operator,ResultEvaluator<ConfigValue,InputValue>> map = new HashMap<>();

    private final ValueHandler<InputValue> valueHandler;

    protected OfferCriteria(Operator operator, ConfigValue propertyValue, ValueHandler<InputValue> valueHandler) {
        this.operator = operator;
        this.propertyValue = propertyValue;
        this.valueHandler = valueHandler;
    }

    public abstract boolean isMatch(InputValue t);

    protected boolean isEquals(){
        return Operator.EQ.equals(operator);
    }
    protected boolean isNotEquals(){
        return Operator.NEQ.equals(operator);
    }
    protected boolean isLessThan(){
        return Operator.LT.equals(operator);
    }

    protected boolean isGreaterThan(){
        return Operator.GT.equals(operator);
    }
    protected boolean isLessThanOrEquals(){
        return Operator.LTE.equals(operator);
    }

    protected boolean isGreaterThanOrEquals(){
        return Operator.GTE.equals(operator);
    }


    /**
     *
     * @param <T>
     * @param <V>
     */
    protected interface ResultEvaluator<T,V>{

        /**
         *
         * @param offerCriteria in context
         * @param value input package propertyvalue
         * @return true if offer criteria is met
         */
        boolean evaluate(OfferCriteria<T,V> offerCriteria, V value);

        /**
         * self registration with offer criteria
         * @param offerCriteria
         */
        void register(OfferCriteria<T,V> offerCriteria);

    }
}
