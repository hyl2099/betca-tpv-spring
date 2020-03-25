package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.api_rest_controllers.CashierClosureResource;
import es.upm.miw.betca_tpv_spring.dtos.CashMovementInputDto;
import es.upm.miw.betca_tpv_spring.dtos.CashierClosureInputDto;
import es.upm.miw.betca_tpv_spring.dtos.CashierStateOutputDto;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static es.upm.miw.betca_tpv_spring.api_rest_controllers.CashierClosureResource.CASHIER_CLOSURES;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class CashierClosureControllerIT {

    @Autowired
    private CashierClosureController cashierClosureController;

    @Test
    void testReadCashierClosureLast() {
        StepVerifier
                .create(cashierClosureController.findCashierClosureLast())
                .expectNextMatches(cashierLastOutputDto -> {
                    assertNotNull(cashierLastOutputDto);
                    assertTrue(cashierLastOutputDto.isClosed());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testPatchCashierDeposit() {
        cashierOpen();
        StepVerifier
                .create(cashierClosureController.deposit(new CashMovementInputDto(BigDecimal.TEN, "")))
                .expectComplete()
                .verify();
        cashierClose();
    }

    @Test
    void testPatchCashierDepositWithCashierClosedBadRequest() {
        StepVerifier
                .create(cashierClosureController.deposit(new CashMovementInputDto(BigDecimal.TEN, "")))
                .expectError()
                .verify();
    }

    @Test
    void testPatchCashierWithdrawal() {
        cashierOpen();
        StepVerifier
                .create(cashierClosureController.deposit(new CashMovementInputDto(BigDecimal.TEN, "")))
                .expectComplete()
                .verify();
        StepVerifier
                .create(cashierClosureController.withdrawal(new CashMovementInputDto(BigDecimal.TEN, "")))
                .expectComplete()
                .verify();
        cashierClose();
    }

    @Test
    void testPatchCashierWithdrawalWithoutCash() {
        cashierOpen();
        StepVerifier
                .create(cashierClosureController.withdrawal(new CashMovementInputDto(BigDecimal.TEN, "")))
                .expectErrorMatches(err -> {
                    assertEquals("Bad Request Exception (400). Not enough cash, you can only withdraw 0€", err.getMessage());
                    return true;
                })
                .verify();
        cashierClose();
    }

    void cashierOpen() {
        StepVerifier
                .create(cashierClosureController.createCashierClosureOpened())
                .expectComplete()
                .verify();
    }

    void cashierClose() {
        StepVerifier
                .create(cashierClosureController.close(new CashierClosureInputDto(BigDecimal.ZERO, BigDecimal.ZERO, "")))
                .expectComplete()
                .verify();
    }

}
