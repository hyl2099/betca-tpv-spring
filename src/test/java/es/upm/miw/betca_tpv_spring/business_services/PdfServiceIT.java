package es.upm.miw.betca_tpv_spring.business_services;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.repositories.BudgetReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.InvoiceReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class PdfServiceIT {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private TicketReactRepository ticketReactRepository;

    @Autowired
    private BudgetReactRepository budgetReactRepository;

    @Autowired
    private InvoiceReactRepository invoiceReactRepository;

    @Test
    void testPdfGenerateTicket() {
        StepVerifier
                .create(this.pdfService.generateTicket(this.ticketReactRepository.findById("201901121")))
                .thenCancel()
                .verify();
    }

    @Test
    void testPdfGenerateBudget() {
        StepVerifier
                .create(this.pdfService.generateBudget(this.budgetReactRepository.findById("test")))
                .thenCancel()
                .verify();
    }

    @Test
    void testPdfGenerateInvoice() {
        StepVerifier
                .create(this.pdfService.generateInvoice(this.invoiceReactRepository.findById("20202")))
                .expectNextMatches(bytes -> {
                    assertNotNull(bytes);
                    assertTrue(bytes.length > 0);
                    return true;
                })
                .expectComplete()
                .verify();
    }

}
