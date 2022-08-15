package com.everestengg.code.challenge.model.csv;


import com.everestengg.code.challenge.domain.offer.OfferCriteria;
import com.everestengg.code.challenge.domain.offer.OfferCriteria.Operator;
import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * captures offer criteria for configured value and input value
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class CsvOfferCriteria {

    @CsvBindByName(column = "offerCriteriaId")
    private int offerCriteriaId;

    @CsvBindByName(column = "property")
    private String property;
    @CsvBindByName(column = "operator")
    private  String operator;

    @CsvBindByName(column = "value")
    private  String propertyValue;

    public OfferCriteria toOfferCriteria(){
        return OfferCriteria.builder().operator(Operator.valueOf(operator))
                .property(property).propertyValue(propertyValue).build();
    }




}
