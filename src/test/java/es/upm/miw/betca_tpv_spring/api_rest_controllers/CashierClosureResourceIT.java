package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.dtos.*;
import es.upm.miw.betca_tpv_spring.repositories.CashierClosureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;

import static es.upm.miw.betca_tpv_spring.api_rest_controllers.CashierClosureResource.CASHIER_CLOSURES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiTestConfig
class CashierClosureResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CashierClosureRepository cashierClosureRepository;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private CashMovementInputDto cashMovementInputDto;

    @BeforeEach
    void init() {
        cashMovementInputDto = new CashMovementInputDto(BigDecimal.TEN, "Moving");
    }

    @Test
    void testFindCashierClosureLast() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CashierLastOutputDto.class)
                .value(Assertions::assertNotNull)
                .value(cashier -> assertTrue(cashier.isClosed()));
    }

    @Test
    void testCreateCashierAndFindCashierClosureLastTotals() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + CASHIER_CLOSURES)
                .exchange()
                .expectStatus().isOk();
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CashierLastOutputDto.class)
                .value(Assertions::assertNotNull)
                .value(cashier -> assertFalse(cashier.isClosed()));
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST
                + CashierClosureResource.STATE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CashierStateOutputDto.class)
                .value(Assertions::assertNotNull);
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST)
                .body(BodyInserters.fromObject(new CashierClosureInputDto(BigDecimal.ZERO, BigDecimal.ZERO, "")))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetCashierClosureLastTotalsWithCashierClosedBadRequest() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST
                + CashierClosureResource.STATE)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPatchCashierDeposit() {
        cashierOpen();
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST
                + CashierClosureResource.DEPOSIT)
                .body(BodyInserters.fromObject(cashMovementInputDto))
                .exchange()
                .expectStatus().isOk();
        CashierStateOutputDto cashierStateOutputDto = this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST + CashierClosureResource.STATE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CashierStateOutputDto.class)
                .returnResult().getResponseBody();
        cashierClosed();
        assertNotNull(cashierStateOutputDto);
        assertEquals(0, cashierStateOutputDto.getTotalCash().compareTo(BigDecimal.TEN));
    }

    @Test
    void testPatchCashierNegativeDeposit() {
        cashierOpen();
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST
                + CashierClosureResource.DEPOSIT)
                .body(BodyInserters.fromObject(new CashMovementInputDto(new BigDecimal(-10), "Moving")))
                .exchange()
                .expectStatus().isBadRequest();
        cashierClosed();
    }

    @Test
    void testPatchCashierDepositWithCashierClosedBadRequest() {
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST
                + CashierClosureResource.DEPOSIT)
                .body(BodyInserters.fromObject(cashMovementInputDto))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPatchCashierWithdrawal() {
        cashierOpen();
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST
                + CashierClosureResource.DEPOSIT)
                .body(BodyInserters.fromObject(new CashMovementInputDto(new BigDecimal(20), "Moving")))
                .exchange()
                .expectStatus().isOk();
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST
                + CashierClosureResource.WITHDRAWAL)
                .body(BodyInserters.fromObject(cashMovementInputDto))
                .exchange()
                .expectStatus().isOk();
        CashierStateOutputDto cashierStateOutputDto = this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST + CashierClosureResource.STATE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CashierStateOutputDto.class)
                .returnResult().getResponseBody();
        cashierClosed();
        assertNotNull(cashierStateOutputDto);
        assertEquals(0, cashierStateOutputDto.getTotalCash().compareTo(BigDecimal.TEN));
    }

    @Test
    void testPatchCashierWithdrawalWithoutCash() {
        cashierOpen();
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST
                + CashierClosureResource.WITHDRAWAL)
                .body(BodyInserters.fromObject(cashMovementInputDto))
                .exchange()
                .expectStatus().isBadRequest();
        cashierClosed();
    }

    void cashierOpen() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + CASHIER_CLOSURES)
                .exchange()
                .expectStatus().isOk();
    }

    void cashierClosed() {
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + CASHIER_CLOSURES + CashierClosureResource.LAST)
                .body(BodyInserters.fromObject(new CashierClosureInputDto(BigDecimal.ZERO, BigDecimal.ZERO, "")))
                .exchange()
                .expectStatus().isOk();
    }
}
