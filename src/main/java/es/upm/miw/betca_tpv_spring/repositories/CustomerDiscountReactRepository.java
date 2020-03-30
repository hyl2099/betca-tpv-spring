package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.CustomerDiscount;
import es.upm.miw.betca_tpv_spring.documents.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface CustomerDiscountReactRepository extends ReactiveSortingRepository<CustomerDiscount, String> {
    Mono<CustomerDiscount> findByUser(Mono<User> user);
}