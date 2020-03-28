package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.documents.Ticket;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class TicketTrackingControllerIT {

    private String TICKET_ID = "201901123";

    @Autowired
    private TicketTrackingController ticketTrackingController;

    @Autowired
    private TicketReactRepository ticketReactRepository;

    @Test
    void testFindByReference() {
        Ticket ticket = this.ticketReactRepository.findById(TICKET_ID).block();
        assert ticket != null;
        StepVerifier
                .create(this.ticketTrackingController.searchByReference(ticket.getReference()))
                .expectNextMatches(t -> {
                    assertEquals(TICKET_ID, t.getId());
                    assertEquals(t.getReference(), ticket.getReference());
                    assertNotNull(t.getShoppingList());
                    assertEquals(t.getShoppingList()[0].getCode(), "8400000000031");
                    assertEquals(t.getShoppingList()[1].getCode(), "8400000000055");
                    return true;
                }).expectComplete().verify();
    }

    @Test
    void testFindByNullReference() {
        StepVerifier
                .create(this.ticketTrackingController.searchByReference(null))
                .expectNextCount(0).expectComplete().verify();
    }

    @Test
    void testFindByEmptyReference() {
        StepVerifier
                .create(this.ticketTrackingController.searchByReference(""))
                .expectNextCount(0).expectComplete().verify();
    }
}