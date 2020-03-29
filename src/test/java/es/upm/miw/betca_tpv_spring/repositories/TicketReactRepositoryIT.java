package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.documents.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class TicketReactRepositoryIT {

    @Autowired
    private TicketReactRepository ticketReactRepository;

    @Test
    void testFindAllAndDatabaseSeeder() {
        StepVerifier
                .create(this.ticketReactRepository.findAll())
                .expectNextMatches(ticket -> {
                    assertEquals("201901121", ticket.getId());
                    assertNotNull(ticket.getCreationDate());
                    assertNotNull(ticket.getReference());
                    assertNotNull(ticket.getShoppingList());
                    assertEquals(0, BigDecimal.TEN.compareTo(ticket.getCard()));
                    assertEquals(0, new BigDecimal("25").compareTo(ticket.getCash()));
                    assertEquals(0, BigDecimal.ZERO.compareTo(ticket.getVoucher()));
                    assertEquals(0, new BigDecimal("20").compareTo(ticket.getTotalCommitted()));
                    assertEquals(0, new BigDecimal("61.7").compareTo(ticket.getTotal()));
                    assertFalse(ticket.toString().matches("@"));
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindByReference() {
        Ticket ticket = this.ticketReactRepository.findById("201901121").block();
        assert ticket != null;
        StepVerifier
                .create(this.ticketReactRepository.findByReference(ticket.getReference()))
                .expectNextMatches(m -> {
                    assertEquals(m.getId(), "201901121");
                    assertEquals(m.getReference(), ticket.getReference());
                    assertNotNull(m.getShoppingList());
                    assertEquals("8400000000017", m.getShoppingList()[0].getArticleId());
                    assertEquals("8400000000024", m.getShoppingList()[1].getArticleId());
                    return true;
                })
                .expectComplete().verify();
    }
}

