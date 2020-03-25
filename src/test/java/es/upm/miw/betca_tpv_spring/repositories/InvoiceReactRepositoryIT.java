package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class InvoiceReactRepositoryIT {

    @Autowired
    private InvoiceReactRepository invoiceReactRepository;

    @Test
    void testFindAllAndDatabaseSeeder() {
        StepVerifier
                .create(this.invoiceReactRepository.findAll())
                .expectNextMatches(invoice -> {
                    assertEquals(1, invoice.simpleId());
                    assertEquals(LocalDate.now().getYear() + "1", invoice.getId());
                    assertNotNull(invoice.getCreationDate());
                    assertNotNull(invoice.getUser());
                    assertNotNull(invoice.getTicket());
                    assertFalse(invoice.toString().matches("@"));
                    return true;
                })
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    void testfindFirstByOrderByCreationDateDescIdDesc(){
        StepVerifier
                .create(this.invoiceReactRepository.findFirstByOrderByCreationDateDescIdDesc())
                .expectNextMatches(invoice -> {
                    assertEquals(2, invoice.simpleId());
                    assertEquals(LocalDate.now().getYear() + "2", invoice.getId());
                    assertNotNull(invoice.getCreationDate());
                    assertNotNull(invoice.getUser());
                    assertNotNull(invoice.getTicket());
                    assertFalse(invoice.toString().matches("@"));
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
