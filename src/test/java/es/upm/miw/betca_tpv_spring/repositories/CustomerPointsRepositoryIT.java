package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.documents.CustomerPoints;
import es.upm.miw.betca_tpv_spring.documents.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Optional;

@TestConfig
public class CustomerPointsRepositoryIT {

    private Optional<User> user;

    @Autowired
    private CustomerPointsReactRepository customerPointsReactRepository;

    @Autowired
    CustomerPointsRepository customerPointsRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    UserReactRepository userReactRepository;

    @BeforeEach
    void seed() {
        user = userRepository.findByMobile("6");
        CustomerPoints customerPoints = new CustomerPoints();
        customerPoints.setUser(user.isPresent() ? user.get() : null);
        this.customerPointsRepository.save(customerPoints);
        System.out.println("Saved" + customerPoints.toString());
    }


    @Test
    void testFinByUser() {
        StepVerifier
                .create(this.customerPointsReactRepository.findByUser(this.userReactRepository.findByMobile("6")))
                .expectNextMatches(cp -> {
                    System.out.println(cp.toString());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
