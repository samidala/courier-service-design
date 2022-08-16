package com.everestengg.code.challenge.repo.offer;

import com.everestengg.code.challenge.domain.offer.OfferCriteria.ValueHandler;
import com.everestengg.code.challenge.exceptions.InvalidOfferCriteriaDefinationException;
import com.everestengg.code.challenge.exceptions.InvalidOfferDefinationException;
import com.everestengg.code.challenge.model.csv.CsvOffer;
import com.everestengg.code.challenge.model.csv.CsvOfferCriteria;
import com.everestengg.code.challenge.domain.offer.Offer;
import com.everestengg.code.challenge.domain.offer.OfferCriteria;
import com.everestengg.code.challenge.util.CsvReader;
import com.everestengg.code.challenge.vo.courier.Package;
import com.everestengg.code.challenge.vo.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        assert offerFilePath !=null : "offer filepath can't be empty";
        List<CsvOffer> csvOffers = csvReader.read(offerFilePath,CsvOffer.class);
        LOGGER.debug("csv offers {} ",csvOffers);
        List<CsvOfferCriteria> csvOfferCriteriaList = new ArrayList<>();
        if(offerCriteriaFilePath != null) {
            csvOfferCriteriaList = csvReader.read(offerCriteriaFilePath,CsvOfferCriteria.class);
        }
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

    private boolean validateCsvOfferCriteriaDef(CsvOfferCriteria csvOfferCriteria){
        validateCsvOffer(csvOfferCriteria);
        validateCsvOfferCriterias(csvOfferCriteria);
        return true;
    }

    private void validateCsvOfferCriterias(CsvOfferCriteria csvOfferCriteria) {
        if(StringUtils.isEmpty(csvOfferCriteria.getPropertyValue())){
            LOGGER.warn("invalid value for property value");
            throw new InvalidOfferCriteriaDefinationException("invalid value for property value");
        }
        String[] values = csvOfferCriteria.getPropertyValue().split("\\|");

        if(values.length == 0){
            LOGGER.warn("values are empty");
            throw new InvalidOfferCriteriaDefinationException(String.format("Invalid property values %s are set",
                    csvOfferCriteria.getPropertyValue()));
        }
        if(Arrays.stream(values).anyMatch(StringUtils::isEmpty)){
            LOGGER.info("invalid values {} ", csvOfferCriteria.getPropertyValue());
            throw new InvalidOfferCriteriaDefinationException("property values should not be empty, current value "
                    + csvOfferCriteria.getPropertyValue());
        }

        if(values.length == 2 && Arrays.stream(values).anyMatch(e-> !e.matches("\\d+"))){
            LOGGER.info("invalid values {} ", csvOfferCriteria.getPropertyValue());
            throw new InvalidOfferCriteriaDefinationException("property values should be numeric "
                    + csvOfferCriteria.getPropertyValue());
        }
    }

    private void validateCsvOffer(CsvOfferCriteria csvOfferCriteria) {
        if(csvOfferCriteria.getOfferCriteriaId() <= 0){
            LOGGER.warn("offer criteria ID should not be null");
            throw new InvalidOfferCriteriaDefinationException("Invalid offer criteria ID");
        }
        if(StringUtils.isEmpty(csvOfferCriteria.getProperty())){
            LOGGER.warn("property should not be null");
            throw new InvalidOfferCriteriaDefinationException("invalid value for property");
        }
        if(!valueHandlerMapDef.containsKey(csvOfferCriteria.getProperty())){
            LOGGER.warn("invalid value for property, no value handler defined for "+ csvOfferCriteria.getProperty());
            throw new
                    InvalidOfferCriteriaDefinationException("invalid value for property, " +
                    "no value handler defined "+ csvOfferCriteria.getProperty());
        }
    }

    private List<Offer> transformToOffer(List<CsvOffer> csvOffers, List<CsvOfferCriteria> offerCriteriaList) {
        offerCriteriaList.forEach(this::validateCsvOfferCriteriaDef);
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
                    if(!offerCriteriaMap.containsKey(Integer.valueOf(ids[i]))){
                        throw new InvalidOfferDefinationException(String.format("offer criteria id %s is missing",
                                ids[i]));
                    }
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
