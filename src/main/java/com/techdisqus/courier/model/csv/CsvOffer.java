package com.techdisqus.courier.model.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public  class CsvOffer {
    @CsvBindByName(column = "offerId")
    private String offerId;
    @CsvBindByName(column = "offerCriteriaIds")
    private String offerCriteriaIds;
    @CsvBindByName(column = "discountPercentage")
    private int percentage;

}
