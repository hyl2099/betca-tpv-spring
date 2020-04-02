package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Invoice;
import es.upm.miw.betca_tpv_spring.documents.Ticket;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface InvoiceReactRepository extends ReactiveSortingRepository<Invoice, String> {
    Mono<Invoice> findFirstByOrderByCreationDateDescIdDesc();
    Mono<Invoice> findFirstByTicketAndTaxGreaterThanEqual(Mono<Ticket> ticket, BigDecimal tax);
}
