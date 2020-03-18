package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.documents.OrderLine;
import es.upm.miw.betca_tpv_spring.dtos.OrderDto;
import es.upm.miw.betca_tpv_spring.dtos.OrderSearchDto;
import es.upm.miw.betca_tpv_spring.repositories.ArticleRepository;
import es.upm.miw.betca_tpv_spring.repositories.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@TestConfig
public class OrderControllerIT {

    @Autowired
    private OrderController orderController;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProviderRepository providerRepository;

    private OrderDto orderDto;

    @BeforeEach
    void seed(){
        OrderLine[] orderLines = {
                new OrderLine(this.articleRepository.findAll().get(0), 10),
                new OrderLine(this.articleRepository.findAll().get(1), 8),
                new OrderLine(this.articleRepository.findAll().get(2), 6),
                new OrderLine(this.articleRepository.findAll().get(3), 4),
        };

        this.orderDto = new OrderDto("order0", this.providerRepository.findAll().get(0), LocalDateTime.now(), orderLines);
    }

    @Test
    void testSearchArticleByDescriptionOrProvider() {
        OrderSearchDto orderSearchDto =
                new OrderSearchDto("null", this.providerRepository.findAll().get(1).getId(), null);
        StepVerifier
                .create(this.orderController.searchOrder(orderSearchDto))
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }
}
