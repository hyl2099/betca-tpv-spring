package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.CustomerPoints;
import es.upm.miw.betca_tpv_spring.documents.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface CustomerPointsReactRepository extends ReactiveSortingRepository<CustomerPoints, String> {
    Mono<CustomerPoints> findByUser(Mono<User> user);
}
