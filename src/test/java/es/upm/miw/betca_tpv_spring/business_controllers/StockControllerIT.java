package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;


@TestConfig
class StockControllerIT {
    @Autowired
    private StockController stockController;

    @Test
    void testReadAll() {
        StepVerifier
                .create(this.stockController.readAll(null, null, null))
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    void testReadAllFilterDateAndStock() {
        StepVerifier
                .create(this.stockController.readAll(5, LocalDateTime.now().minusMonths(1), LocalDateTime.now()))
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void testArticleInfo() {
        StepVerifier
                .create(this.stockController.getArticleInfo(null))
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    void testArticleInfoWithMinimumStock() {
        StepVerifier
                .create(this.stockController.getArticleInfo(10))
                .expectNextCount(9)
                .expectComplete()
                .verify();
    }

    @Test
    void testArticleInfoWithMinimumStockNoResult() {
        StepVerifier
                .create(this.stockController.getArticleInfo(-10))
                .expectComplete()
                .verify();
    }

    @Test
    void testGetSoldUnits() {
        HashMap<String, Integer> unitsMap = new HashMap<>();
        unitsMap.put("8400000000017", 2);
        unitsMap.put("8400000000031", 12);
        unitsMap.put("8400000000055", 12);
        unitsMap.put("8400000000024", 3);
        StepVerifier
                .create(this.stockController.getSoldUnits(null, null))
                .expectNextMatches(articleStockDto -> articleStockDto.getSoldUnits().equals(unitsMap.get(articleStockDto.getCode())))
                .expectNextMatches(articleStockDto -> articleStockDto.getSoldUnits().equals(unitsMap.get(articleStockDto.getCode())))
                .expectNextMatches(articleStockDto -> articleStockDto.getSoldUnits().equals(unitsMap.get(articleStockDto.getCode())))
                .expectNextMatches(articleStockDto -> articleStockDto.getSoldUnits().equals(unitsMap.get(articleStockDto.getCode())))
                .expectComplete()
                .verify();
    }

    @Test
    void testGetShopping() {
        StepVerifier
                .create(this.stockController.getShopping(null, null))
                .expectNextCount(11)
                .expectComplete()
                .verify();
    }

    @Test
    void testGetShoppingInitDate() {
        StepVerifier
                .create(this.stockController.getShopping(LocalDateTime.now().with(LocalTime.of(0, 0)), null))
                .expectNextCount(11)
                .expectComplete()
                .verify();
    }

    @Test
    void testGetShoppingEndDate() {
        StepVerifier
                .create(this.stockController.getShopping(null, LocalDateTime.now().minusMonths(1)))
                .expectComplete()
                .verify();
    }
}
