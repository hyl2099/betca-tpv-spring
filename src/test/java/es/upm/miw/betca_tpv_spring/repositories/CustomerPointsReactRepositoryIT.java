package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
public class CustomerPointsReactRepositoryIT {

    @Autowired
    private CustomerPointsReactRepository customerPointsReactRepository;

    @Autowired
    private UserReactRepository userReactRepository;

    @Test
    void testFinByUser() {
        StepVerifier
                .create(this.customerPointsReactRepository.findByUser(this.userReactRepository.findByMobile("666666000")))
                .expectNextMatches(cp -> {
                    assertNotNull(cp);
                    assertEquals("cp1", cp.getId());
                    assertEquals(10, cp.getPoints());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void findCustomerPointsByUserAndProjectPointsTest() {
        StepVerifier
                .create(this.customerPointsReactRepository.findByUser(
                        this.userReactRepository.findByMobile("666666001")))
                .expectNextMatches(cp -> {
                    assertNotNull(cp);
                    assertEquals("cp2", cp.getId());
                    assertEquals(20, cp.getPoints());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
