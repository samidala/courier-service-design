package com.everestengg.code.challenge.repo.offer;

import com.everestengg.code.challenge.domain.offer.OfferCriteria.ValueHandler;
import com.everestengg.code.challenge.exceptions.InvalidOfferCriteriaDefinationException;
import com.everestengg.code.challenge.exceptions.InvalidOfferDefinationException;
import com.everestengg.code.challenge.model.csv.CsvOffer;
import com.everestengg.code.challenge.model.csv.CsvOfferCriteria;
import com.everestengg.code.challenge.domain.offer.Offer;
import com.everestengg.code.challenge.domain.offer.OfferCriteria;
import com.everestengg.code.challenge.util.CsvReader;
import com.everestengg.code.challenge.vo.Package;
import com.everestengg.code.challenge.vo.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.everestengg.code.challenge.domain.offer.OfferCriteria.getPackageValueHandlerMap;

public class CsvOfferRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvOfferRepository.class);

    private static final Map<String, ValueHandler<Package, String>> valueHandlerMapDef = getPackageValueHandlerMap();

    private CsvReader csvReader;
    public CsvOfferRepository(){
        csvReader = new CsvReader();
    }

    public void setCsvReader(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public Response<List<Offer>> prepareOffers(String offerFilePath, String offerCriteriaFilePath) throws IOException {
        List<CsvOffer> csvOffers = csvReader.read(offerFilePath,CsvOffer.class);
        LOGGER.debug("csv offers {} ",csvOffers);
        List<CsvOfferCriteria> csvOfferCriteriaList = csvReader.read(offerCriteriaFilePath,CsvOfferCriteria.class);
        LOGGER.debug("csvOfferCriteriaList {} ",csvOfferCriteriaList);


        List<Offer> offerList = transformToOffer(csvOffers, csvOfferCriteriaList);
        LOGGER.trace("offers {} ",offerList);
        return Response.<List<Offer>>builder().result(offerList).build();
    }

    private boolean validateCsvOffer(CsvOffer csvOffer){
        if(StringUtils.isEmpty(csvOffer.getOfferId())){
            throw new InvalidOfferDefinationException("offer ID should not be null or empty");
        }
        if(csvOffer.getPercentage() <= 0){
            throw new InvalidOfferDefinationException("invalid percentage value");
        }
        return true;
    }

    private boolean validateCsvOfferCriteria(CsvOfferCriteria csvOfferCriteria){
        if(csvOfferCriteria.getOfferCriteriaId() <= 0){
            LOGGER.warn("offer criteria ID should not be null");
            throw new InvalidOfferCriteriaDefinationException("Invalid offer criteria ID");
        }
        if(StringUtils.isEmpty(csvOfferCriteria.getProperty())){
            LOGGER.warn("property should not be null");
            throw new InvalidOfferCriteriaDefinationException("invalid value for property");
        }
        if(!valueHandlerMapDef.containsKey(csvOfferCriteria.getProperty())){
            LOGGER.warn("invalid value for property, no value handler defined for "+csvOfferCriteria.getProperty());
            throw new
                    InvalidOfferCriteriaDefinationException("invalid value for property, " +
                    "no value handler defined "+csvOfferCriteria.getProperty());
        }

        if(StringUtils.isEmpty(csvOfferCriteria.getPropertyValue())){
            LOGGER.warn("invalid value for property value");
            throw new InvalidOfferCriteriaDefinationException("invalid value for property value");
        }

        return true;
    }

    private List<Offer> transformToOffer(List<CsvOffer> csvOffers, List<CsvOfferCriteria> offerCriteriaList) {
        offerCriteriaList.forEach(this::validateCsvOfferCriteria);
        Map<Integer, CsvOfferCriteria> offerCriteriaMap = offerCriteriaList.stream().
                collect(Collectors.toMap(CsvOfferCriteria::getOfferCriteriaId, Function.identity()));
        List<Offer> offerList = new ArrayList<>(csvOffers.size());
        for(CsvOffer csvOffer : csvOffers){
            validateCsvOffer(csvOffer);
            OfferCriteria[] offerCriterias = null;
            if(StringUtils.isNotEmpty(csvOffer.getOfferCriteriaIds())){
                String[] ids = csvOffer.getOfferCriteriaIds().split("\\|");
                offerCriterias = new OfferCriteria[ids.length];
                for(int i = 0; i < ids.length; i++){
                    offerCriterias[i] = offerCriteriaMap.get(Integer.valueOf(ids[i])).toOfferCriteria();

                }
            }
            Offer offer = new Offer(csvOffer.getOfferId(), csvOffer.getPercentage(),offerCriterias);
            offerList.add(offer);
            LOGGER.debug("loaded offer is {}",offer);
        }
        return offerList;
    }

}
