package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
public class CustomerPointsControllerIT {

    @Autowired
    private CustomerPointsController customerPointsController;

    @Test
    void testConsumeAllCustomerPointsByUserMobile() {
        StepVerifier
                .create(this.customerPointsController.consumeAllCustomerPointsByUserMobile("666666002"))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @Test
    void sendCustomerPointsByUserMobileTest() {
        StepVerifier
                .create(this.customerPointsController.sendCustomerPointsByUserMobile("666666003"))
                .expectNextMatches(cp -> {
                    assertNotNull(cp);
                    assertEquals(40, cp.intValue());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
