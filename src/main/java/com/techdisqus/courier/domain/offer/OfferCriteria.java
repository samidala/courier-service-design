package com.techdisqus.courier.domain.offer;


import com.techdisqus.courier.vo.courier.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * captures offer criteria for configured value and input value
 *
 */
@Getter
@Builder
@AllArgsConstructor
@ToString
public class OfferCriteria {

    private String property;

    private List<String> propertyValues;

    private final ValueHandler valueHandler;


    public  boolean isMatch(String inputVal){

        if(propertyValues.size() == 1){
            return inputVal.equals(propertyValues.get(0));
        }else{
            return Double.valueOf(inputVal).compareTo(Double.valueOf(propertyValues.get(0))) >= 0 &&
                    Double.valueOf(inputVal).compareTo(Double.valueOf(propertyValues.get(1))) <= 0;
        }
    }

    public interface ValueHandler<E,V>{
        V getValue(E e);
    }

    public static final OfferCriteria.ValueHandler<Package,String> distanceValueHandler = aPackage -> String.valueOf(aPackage.getDist());
    public static final OfferCriteria.ValueHandler<Package,String> weightValueHandler = aPackage -> String.valueOf(aPackage.getWeight());
    public static final OfferCriteria.ValueHandler<Package,String> categoryValueHandler = aPackage -> String.valueOf(aPackage.getCategory());

    public static Map<String,ValueHandler<Package,String>> getPackageValueHandlerMap(){
        Map<String,ValueHandler<Package,String>> valueHandlerMapDef = new HashMap<>(3);
        valueHandlerMapDef.put("dist",OfferCriteria.distanceValueHandler);
        valueHandlerMapDef.put("weight",OfferCriteria.weightValueHandler);
        valueHandlerMapDef.put("category",OfferCriteria.categoryValueHandler);
        return  valueHandlerMapDef;
    }


}
