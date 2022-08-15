package com.everestengg.code.challenge.model.offer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * captures offer criteria for configured value and input value
 *
 */
@Getter
@Builder
@AllArgsConstructor
@ToString
public class OfferCriteria {

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

    private String property;

    private  Operator operator;

    private  String propertyValue;


    public  boolean isMatch(String inputVal){
        if(isEquals()){
            return Double.valueOf(inputVal).equals(Double.valueOf(propertyValue));
        }else if(isNotEquals()){
            return !Double.valueOf(inputVal).equals(Double.valueOf(propertyValue));
        }else if(isGreaterThan()){
            return Double.valueOf(inputVal).compareTo(Double.valueOf(propertyValue)) > 0;
        }else if(isLessThan()){
            return Double.valueOf(inputVal).compareTo(Double.valueOf(propertyValue)) < 0;
        }else if(isGreaterThanOrEquals()){
            return Double.valueOf(inputVal).compareTo(Double.valueOf(propertyValue)) >= 0;
        }else if(isLessThanOrEquals()){
            return Double.valueOf(inputVal).compareTo(Double.valueOf(propertyValue)) <= 0;
        }else if(isRange()){
            String[] values = propertyValue.split("\\|");
            return Double.valueOf(inputVal).compareTo(Double.valueOf(values[0])) >= 0 &&
                    Double.valueOf(inputVal).compareTo(Double.valueOf(values[1])) <= 0;
        }
        return false;
    }

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

    protected boolean isRange(){
        return Operator.RANGE.equals(operator);
    }


}
