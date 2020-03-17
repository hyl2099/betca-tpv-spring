package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.documents.Provider;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface OrderReactRepository extends ReactiveSortingRepository<Order, String> {

    Flux<Order> findByDescriptionLikeOrProviderAndClosingDateIsNull(String description, Provider provider, LocalDateTime closingDate);

    Flux<Order> findByDescriptionLikeOrProvider(String description, Provider provider);
}
