package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.dtos.CashierClosureInputDto;
import es.upm.miw.betca_tpv_spring.dtos.ShoppingDto;
import es.upm.miw.betca_tpv_spring.dtos.TicketCreationInputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.util.Collections;

import static es.upm.miw.betca_tpv_spring.api_rest_controllers.CashierClosureResource.CASHIER_CLOSURES;

@ApiTestConfig
public class InvoiceResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Test
    void testCreate() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + InvoiceResource.INVOICES)
                .exchange()
                .expectStatus().isOk()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
    }
}
