package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.data_services.DatabaseSeederService;
import es.upm.miw.betca_tpv_spring.documents.Shopping;
import es.upm.miw.betca_tpv_spring.documents.ShoppingState;
import es.upm.miw.betca_tpv_spring.documents.Ticket;
import es.upm.miw.betca_tpv_spring.repositories.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
public class InvoiceControllerIT {

    @Autowired
    private InvoiceController invoiceController;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @AfterEach
    void initialize() {
        databaseSeederService.deleteAllAndInitializeAndSeedDataBase();
    }

    @Test
    void testCreateInvoice() {
        StepVerifier
                .create(this.invoiceController.createInvoice())
                .expectNextMatches(invoice -> {
                    assertNotNull(invoice.getCreationDate());
                    assertNotNull(invoice.getTicket());
                    assertNotNull(invoice.getUser());
                    assertEquals(new BigDecimal("0.662400"), invoice.getBaseTax());
                    assertEquals(new BigDecimal("0.027600"), invoice.getTax());
                    return true;
                })
                .expectComplete()
                .verify();

    }

    @Test
    void testCreateInvoiceErrorUserNotCompleted() {
        Optional<Ticket> ticketOptional = ticketRepository.findById("201901125");
        ticketOptional.ifPresent(ticket -> ticketRepository.delete(ticket));
        StepVerifier
                .create(this.invoiceController.createInvoice())
                .expectErrorMatches(error -> {
                    assertEquals("Bad Request Exception (400). User not completed", error.getMessage());
                    return true;
                })
                .verify();
    }

    @Test
    void testCreateInvoiceErrorNotTicket() {
        ticketRepository.deleteAll();
        StepVerifier
                .create(this.invoiceController.createInvoice())
                .expectErrorMatches(error -> {
                    assertEquals("Not Found Exception (404). Last Ticket not found", error.getMessage());
                    return true;
                })
                .verify();
    }

    @Test
    void testCreateInvoiceErrorTicketIsDebt() {
        ticketRepository.deleteById("201901122");
        ticketRepository.deleteById("201901123");
        ticketRepository.deleteById("201901124");
        ticketRepository.deleteById("201901125");
        StepVerifier
                .create(this.invoiceController.createInvoice())
                .expectErrorMatches(error -> {
                    assertEquals("Bad Request Exception (400). Ticket is debt", error.getMessage());
                    return true;
                })
                .verify();
    }

    @Test
    void testCreateInvoiceErrorArticleNotFound() {
        Optional<Ticket> ticketOptional = ticketRepository.findById("201901125");
        ticketOptional.ifPresent(ticket -> {
            Shopping[] shoppingList = {new Shopping(2, BigDecimal.ZERO, ShoppingState.COMMITTED, "875675567",
                    "aaa", BigDecimal.TEN)};
            ticket.setShoppingList(shoppingList);
            ticketRepository.save(ticket);
        });
        StepVerifier
                .create(this.invoiceController.createInvoice())
                .expectErrorMatches(error -> {
                    assertEquals("Not Found Exception (404). Article(875675567)", error.getMessage());
                    return true;
                })
                .verify();
    }
}
