package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.dtos.TicketOutputDto;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class TicketTrackingController {

    private TicketReactRepository ticketReactRepository;

    @Autowired
    public TicketTrackingController(TicketReactRepository ticketReactRepository) {
        this.ticketReactRepository = ticketReactRepository;
    }

    Mono<TicketOutputDto> searchByReference(String reference) {
        return this.ticketReactRepository.findByReference(reference)
                .switchIfEmpty(Mono.empty()).map(TicketOutputDto::new);
    }
}