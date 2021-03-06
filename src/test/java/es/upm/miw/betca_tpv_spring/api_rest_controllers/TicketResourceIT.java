package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.dtos.CashierClosureInputDto;
import es.upm.miw.betca_tpv_spring.dtos.ShoppingDto;
import es.upm.miw.betca_tpv_spring.dtos.TicketCreationInputDto;
import es.upm.miw.betca_tpv_spring.dtos.TicketOutputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static es.upm.miw.betca_tpv_spring.api_rest_controllers.CashierClosureResource.CASHIER_CLOSURES;
import static es.upm.miw.betca_tpv_spring.api_rest_controllers.TicketResource.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ApiTestConfig
class TicketResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Test
    void testCreateTicket() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + CASHIER_CLOSURES)
                .exchange()
                .expectStatus().isOk();
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "prueba", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), true);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(null, new BigDecimal("100.00")
                , BigDecimal.ZERO, BigDecimal.ZERO, Collections.singletonList(shoppingDto), "Nota del ticket...");
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + TicketResource.TICKETS)
                .body(BodyInserters.fromObject(ticketCreationInputDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST)
                .body(BodyInserters.fromObject(new CashierClosureInputDto(BigDecimal.ZERO, BigDecimal.ZERO, "")))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testCreateReserve() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + CASHIER_CLOSURES)
                .exchange()
                .expectStatus().isOk();
        ShoppingDto shoppingDto =
                new ShoppingDto("1", "", new BigDecimal("100.00"), 1, BigDecimal.ZERO,
                        new BigDecimal("100.00"), false);
        TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto("666666004",
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, Collections.singletonList(shoppingDto),
                "Nota del ticket...");
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + TicketResource.TICKETS)
                .body(BodyInserters.fromObject(ticketCreationInputDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST)
                .body(BodyInserters.fromObject(new CashierClosureInputDto(BigDecimal.ZERO, BigDecimal.ZERO, "")))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testSearchByMobileDateOrAmount() {
        List<TicketOutputDto> tickets = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                .path(contextPath + TICKETS + SEARCH)
                .queryParam("mobile", "666666004")
                .queryParam("date", LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).toString())
                .queryParam("amount", 6)
                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketOutputDto.class)
                .returnResult().getResponseBody();
        assertNotNull(tickets);
        assertEquals(1, tickets.size());
    }

    @Test
    void testSearchMissParam() {
        List<TicketOutputDto> tickets = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + TICKETS + SEARCH)
                        .queryParam("date", LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).toString())
                        .queryParam("amount", 6)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketOutputDto.class)
                .returnResult().getResponseBody();
        assertNotNull(tickets);
        assertEquals(4, tickets.size());
    }

    @Test
    void testSearchNotCommittedByArticle() {
        String articleId = "8400000000024";
        List<TicketOutputDto> tickets = this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + TICKETS + SEARCH_BY_ARTICLE, articleId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketOutputDto.class)
                .returnResult().getResponseBody();
        assertNotNull(tickets);
        assertEquals(1, tickets.size());
    }
}
