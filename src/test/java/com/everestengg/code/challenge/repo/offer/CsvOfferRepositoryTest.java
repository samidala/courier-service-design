package com.everestengg.code.challenge.repo.offer;

import com.everestengg.code.challenge.domain.offer.Offer;
import com.everestengg.code.challenge.domain.offer.OfferCriteria;
import com.everestengg.code.challenge.model.csv.CsvOffer;
import com.everestengg.code.challenge.model.csv.CsvOfferCriteria;
import com.everestengg.code.challenge.util.CsvReader;
import com.everestengg.code.challenge.vo.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class CsvOfferRepositoryTest {

    @Mock
    private CsvReader csvReaderMock;
    @Test
    void testLoadOffers() throws IOException {
        CsvOfferRepository csvOfferRepository = new CsvOfferRepository();
        csvOfferRepository.setCsvReader(csvReaderMock);
                List<CsvOffer> csvOfferList = new ArrayList<>();
        addCsvOffers(csvOfferList);
        Mockito.lenient().when(csvReaderMock.read("offers.csv",CsvOffer.class)).thenReturn(csvOfferList);
        List<CsvOfferCriteria> csvOfferCriteriaList = new ArrayList<>();

        addCsvOfferCriterias(csvOfferCriteriaList);
        Mockito.lenient().when(csvReaderMock.read("offercriterias.csv",CsvOfferCriteria.class))
                .thenReturn(csvOfferCriteriaList);

        Response<List<Offer>> result = csvOfferRepository.prepareOffers("offers.csv",
                "offercriterias.csv");
        Assertions.assertEquals(3, result.getResult().size());
        assertOfferIds(result);

        assertOfferCriteria(result, 0,"0|200", "70|200");
        assertOfferCriteria(result, 1,  "50|150", "100|250");
        assertOfferCriteria(result, 2,  "50|250", "10|150");
    }

    private void assertOfferCriteria(Response<List<Offer>> result, int index,
                                     String offerCriteria1expected, String offerCriteria2Expected) {
        Assertions.assertEquals("dist", result.getResult().get(index).getOfferCriterias()[0].getProperty());
        Assertions.assertEquals(Arrays.asList(offerCriteria1expected.split("\\|")),
                result.getResult().get(index).getOfferCriterias()[0].getPropertyValues());

        Assertions.assertEquals("weight", result.getResult().get(index).getOfferCriterias()[1].getProperty());
        Assertions.assertEquals(Arrays.asList(offerCriteria2Expected.split("\\|")),
                result.getResult().get(index).getOfferCriterias()[1].getPropertyValues());
    }

    private void assertOfferIds(Response<List<Offer>> result) {
        Assertions.assertEquals("OFR001", result.getResult().get(0).getOfferId());
        Assertions.assertEquals("OFR002", result.getResult().get(1).getOfferId());
        Assertions.assertEquals("OFR003", result.getResult().get(2).getOfferId());
    }

    private void addCsvOfferCriterias(List<CsvOfferCriteria> csvOfferCriteriaList) {
        csvOfferCriteriaList.add(CsvOfferCriteria.builder().offerCriteriaId(1)
                                     .property("dist").propertyValue("0|200").build());

        csvOfferCriteriaList.add(CsvOfferCriteria
                                .builder().offerCriteriaId(2)
                                .property("weight").propertyValue("70|200").build());

        csvOfferCriteriaList.add(CsvOfferCriteria
                .builder().offerCriteriaId(3)
                .property("dist").propertyValue("50|150").build());

        csvOfferCriteriaList.add(CsvOfferCriteria
                .builder().offerCriteriaId(4)
                .property("weight").propertyValue("100|250").build());

        csvOfferCriteriaList.add(CsvOfferCriteria
                .builder().offerCriteriaId(5)
                .property("dist").propertyValue("50|250").build());

        csvOfferCriteriaList.add(CsvOfferCriteria
                .builder().offerCriteriaId(6)
                .property("weight").propertyValue("10|150").build());
    }

    private void addCsvOffers(List<CsvOffer> csvOfferList) {
        csvOfferList.add(CsvOffer.builder().offerId("OFR001").percentage(10).offerCriteriaIds("1|2").build());
        csvOfferList.add(CsvOffer.builder().offerId("OFR002").percentage(7).offerCriteriaIds("3|4").build());
        csvOfferList.add(CsvOffer.builder().offerId("OFR003").percentage(5).offerCriteriaIds("5|6").build());
    }
}
