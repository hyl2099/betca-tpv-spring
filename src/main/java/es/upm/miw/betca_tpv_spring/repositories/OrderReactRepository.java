package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.documents.Provider;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface OrderReactRepository extends ReactiveSortingRepository<Order, String> {

    Flux<Order> findByDescriptionLikeOrProvider(String description, String provider);
}
