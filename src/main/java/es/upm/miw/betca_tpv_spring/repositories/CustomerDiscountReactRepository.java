package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.CustomerDiscount;
import es.upm.miw.betca_tpv_spring.dtos.TicketOutputDto;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerDiscountReactRepository extends ReactiveSortingRepository<CustomerDiscount, String> {
    Mono<CustomerDiscount> findByUserMobile(String mobile);
}