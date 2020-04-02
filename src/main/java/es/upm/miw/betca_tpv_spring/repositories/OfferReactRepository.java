package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Offer;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface OfferReactRepository extends ReactiveSortingRepository<Offer, String> {
    Flux<Offer> findAllByRegistrationDateBetween(LocalDateTime registrationDate, LocalDateTime expirationDate);
}
