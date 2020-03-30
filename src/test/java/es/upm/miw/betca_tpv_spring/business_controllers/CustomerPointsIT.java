package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;

import es.upm.miw.betca_tpv_spring.documents.CustomerPoints;
import es.upm.miw.betca_tpv_spring.documents.User;
import es.upm.miw.betca_tpv_spring.repositories.CustomerPointsReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.CustomerPointsRepository;
import es.upm.miw.betca_tpv_spring.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Optional;

@TestConfig
public class CustomerPointsIT {
    private Optional<User> user;

    @Autowired
    private CustomerPointsController customerPointsController;

    @Autowired
    private CustomerPointsRepository customerPointsRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void seed() {
        user = userRepository.findByMobile("6");
        CustomerPoints customerPoints = new CustomerPoints();
        customerPoints.setPoints(100);
        customerPoints.setUser(user.isPresent() ? user.get() : null);
        this.customerPointsRepository.save(customerPoints);
        System.out.println("Saved" + customerPoints.toString());
    }

    @Test
    void testConsumeAllCustomerPointsByUserMobile() {
        StepVerifier
                .create(this.customerPointsController.consumeAllCustomerPointsByUserMobile("6"))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}
