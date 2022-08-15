package com.everestengg.code.challenge.offer.repo;

import com.everestengg.code.challenge.model.csv.CsvOffer;
import com.everestengg.code.challenge.model.csv.CsvOfferCriteria;
import com.everestengg.code.challenge.domain.offer.Offer;
import com.everestengg.code.challenge.domain.offer.OfferCriteria;
import com.everestengg.code.challenge.util.CsvReader;
import com.everestengg.code.challenge.vo.Response;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvOfferRepository {

    public Response<Boolean> prepareOffers(String offerFilePath, String offerCriteriaFilePath) throws IOException {

        List<CsvOffer> offers = CsvReader.read(offerFilePath,CsvOffer.class);
        List<CsvOfferCriteria> offerCriteriaList = CsvReader.read(offerCriteriaFilePath,CsvOfferCriteria.class);
        Map<Integer, CsvOfferCriteria> offerCriteriaMap = offerCriteriaList.stream().
                collect(Collectors.toMap(CsvOfferCriteria::getOfferCriteriaId, Function.identity()));


        for(CsvOffer csvOffer : offers){
            OfferCriteria[] offerCriterias = null;
            if(StringUtils.isNotEmpty(csvOffer.getOfferCriteriaIds())){
                String[] ids = csvOffer.getOfferCriteriaIds().split("\\|");
                offerCriterias = new OfferCriteria[ids.length];
                for(int i = 0; i < ids.length; i++){
                    offerCriterias[i] = offerCriteriaMap.get(Integer.valueOf(ids[i])).toOfferCriteria();

                }
            }
            Offer offer = new Offer(csvOffer.getOfferId(), csvOffer.getPercentage(),offerCriterias);
            System.out.println(offer);
        }


        return Response.<Boolean>builder().result(true).build();
    }

    public static void main(String[] args) throws IOException {
        CsvOfferRepository csvOfferRepository = new CsvOfferRepository();
        csvOfferRepository.prepareOffers("D:\\interview\\courier-service-design\\src\\main\\resources\\offers.csv",
                "D:\\interview\\courier-service-design\\src\\main\\resources\\offercriterias.csv");
    }
}
