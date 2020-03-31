package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.TicketTrackingController;
import es.upm.miw.betca_tpv_spring.dtos.TicketOutputDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(TicketTrackingResource.TICKET_TRACKING)
public class TicketTrackingResource {

    static final String TICKET_TRACKING = "/ticket-tracking";
    static final String REFERENCE = "/{reference}";

    private TicketTrackingController ticketTrackingController;

    @Autowired
    public TicketTrackingResource(TicketTrackingController ticketTrackingController) {
        this.ticketTrackingController = ticketTrackingController;
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = REFERENCE)
    public Mono<TicketOutputDto> searchByReference(@PathVariable String reference) {
        return this.ticketTrackingController.searchByReference(reference)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
