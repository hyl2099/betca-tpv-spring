package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.CashierClosure;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CashierClosureReactRepository extends ReactiveSortingRepository<CashierClosure, String> {

    Mono<CashierClosure> findFirstByOrderByOpeningDateDesc();

    Flux<CashierClosure> findAllByClosureDateNotNull();

    Flux<CashierClosure> findByClosureDateBetween(LocalDateTime iniDate, LocalDateTime finalDate);

    Flux<CashierClosure> findByFinalCashGreaterThanEqual(BigDecimal finalCash);

    Flux<CashierClosure> findByClosureDateBetweenAndFinalCashGreaterThanEqual(LocalDateTime iniDate, LocalDateTime finalDate, BigDecimal finalCash);
}
