package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.data_services.DatabaseSeederService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@TestConfig
public class CustomerDiscountReactRepositoryIT {

    @Autowired
    private CustomerDiscountReactRepository customerDiscountReactRepository;

    @Autowired
    private UserReactRepository userReactRepository;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Test
    void testFindAllAndDatabaseSeeder() {
        StepVerifier
                .create(this.customerDiscountReactRepository.findAll())
                .expectNextMatches(customerDiscount -> {
                    assertNotNull(customerDiscount.getId());
                    assertEquals("discount1", customerDiscount.getDescription());
                    assertNotNull(customerDiscount.getRegistrationDate());
                    assertEquals(BigDecimal.TEN, customerDiscount.getDiscount());
                    assertEquals(BigDecimal.TEN, customerDiscount.getMinimumPurchase());
                    assertNotNull(customerDiscount.getUser());
                    assertFalse(customerDiscount.toString().matches("@"));
                    return true;
                })
                .thenCancel()
                .verify();
    }
}