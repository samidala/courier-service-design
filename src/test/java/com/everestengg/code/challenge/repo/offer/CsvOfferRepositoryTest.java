package com.everestengg.code.challenge.repo.offer;

import com.everestengg.code.challenge.domain.offer.Offer;
import com.everestengg.code.challenge.vo.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class CsvOfferRepositoryTest {

    @Test
    void testLoadOffers() throws IOException {
        Response<List<Offer>> result = new CsvOfferRepository().prepareOffers("D:\\interview\\courier-service-design\\src\\main\\resources\\offers.csv",
                "D:\\interview\\courier-service-design\\src\\main\\resources\\offercriterias.csv");
        Assertions.assertEquals(3, result.getResult().size());
    }
}
