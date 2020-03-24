package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.repositories.ArticleReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.InvoiceReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
public class InvoiceControllerIT {

    @Autowired
    private InvoiceController invoiceController;

    @Test
    void testCreateInvoice() {
        StepVerifier
                .create(this.invoiceController.createInvoice())
                .expectNextMatches(invoice -> {
                    assertNotNull(invoice.getCreationDate());
                    assertNotNull(invoice.getTicket());
                    assertNotNull(invoice.getUser());
                    assertEquals(new BigDecimal("15.6216"), invoice.getBaseTax());
                    assertEquals(new BigDecimal("0.0184"), invoice.getTax());
                    return true;
                })
                .expectComplete()
                .verify();

    }


}
