package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.TicketController;
import es.upm.miw.betca_tpv_spring.documents.Ticket;
import es.upm.miw.betca_tpv_spring.dtos.TicketCreationInputDto;
import es.upm.miw.betca_tpv_spring.dtos.TicketOutputDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(TicketResource.TICKETS)
public class TicketResource {

    public static final String TICKETS = "/tickets";
    public static final String TICKET_ID = "/{id}";

    private TicketController ticketController;

    @Autowired
    public TicketResource(TicketController ticketController) {
        this.ticketController = ticketController;
    }

    @PostMapping(produces = {"application/pdf", "application/json"})
    public Mono<byte[]> createTicket(@Valid @RequestBody TicketCreationInputDto ticketCreationDto) {
        return this.ticketController.createTicketAndPdf(ticketCreationDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping
    public Flux<TicketOutputDto> readAll() {
        return this.ticketController.readAll()
                .doOnEach(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = TICKET_ID + "/pdf")
    public byte[] getPdf(@PathVariable String id) {
        return this.ticketController.getPdf(id);
    }

    @GetMapping(value = TICKET_ID )
    public Mono<Ticket> getTicket(@PathVariable String id) {
        return this.ticketController.getTicket(id);
    }
}
