package com.techdisqus.courier.model.csv;


import com.techdisqus.courier.domain.offer.OfferCriteria;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

/**
 * captures offer criteria for configured value and input value
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CsvOfferCriteria {

    @CsvBindByName(column = "offerCriteriaId")
    private int offerCriteriaId;

    @CsvBindByName(column = "property")
    private String property;

    @CsvBindByName(column = "value")
    private  String propertyValue;

    public OfferCriteria toOfferCriteria(){
        return OfferCriteria.builder()
                .property(property).
                propertyValues(Arrays.asList(propertyValue.split("\\|")))
                .valueHandler(OfferCriteria.getPackageValueHandlerMap().get(property))
                .build();
    }




}
