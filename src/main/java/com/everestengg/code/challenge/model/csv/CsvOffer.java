package com.everestengg.code.challenge.model.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public  class CsvOffer {
    @CsvBindByName(column = "offerId")
    private String offerId;
    @CsvBindByName(column = "offerCriteriaIds")
    private String offerCriteriaIds;
    @CsvBindByName(column = "discountPercentage")
    private int percentage;

}
