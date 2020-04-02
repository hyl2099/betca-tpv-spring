package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.data_services.DatabaseSeederService;
import es.upm.miw.betca_tpv_spring.documents.Quarter;
import es.upm.miw.betca_tpv_spring.documents.Shopping;
import es.upm.miw.betca_tpv_spring.documents.ShoppingState;
import es.upm.miw.betca_tpv_spring.documents.Ticket;
import es.upm.miw.betca_tpv_spring.repositories.InvoiceReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class InvoiceControllerIT {

    @Autowired
    private InvoiceController invoiceController;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Autowired
    private InvoiceReactRepository invoiceReactRepository;

    @AfterEach
    void initialize() {
        databaseSeederService.deleteAllAndInitializeAndSeedDataBase();
    }

    @Test
    void testCreateInvoice() {
        StepVerifier
                .create(this.invoiceController.createAndPdf())
                .expectNextMatches(invoice -> {
                    assertNotNull(invoice);
                    assertTrue(invoice.length > 0);
                    return true;
                })
                .expectComplete()
                .verify();

        StepVerifier
                .create(this.invoiceReactRepository.findById("20203"))
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
        Optional<Ticket> ticketOptional1 = ticketRepository.findById("201901125");
        ticketOptional1.ifPresent(ticket -> ticketRepository.delete(ticket));
        Optional<Ticket> ticketOptional2 = ticketRepository.findById("201901126");
        ticketOptional2.ifPresent(ticket -> ticketRepository.delete(ticket));
        StepVerifier
                .create(this.invoiceController.createAndPdf())
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
                .create(this.invoiceController.createAndPdf())
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
        ticketRepository.deleteById("201901126");
        StepVerifier
                .create(this.invoiceController.createAndPdf())
                .expectErrorMatches(error -> {
                    assertEquals("Bad Request Exception (400). Ticket is debt", error.getMessage());
                    return true;
                })
                .verify();
    }

    @Test
    void testCreateInvoiceErrorArticleNotFound() {
        Optional<Ticket> ticketOptional = ticketRepository.findById("201901126");
        ticketOptional.ifPresent(ticket -> {
            Shopping[] shoppingList = {new Shopping(2, BigDecimal.ZERO, ShoppingState.COMMITTED, "875675567",
                    "aaa", BigDecimal.TEN)};
            ticket.setShoppingList(shoppingList);
            ticketRepository.save(ticket);
        });
        StepVerifier
                .create(this.invoiceController.createAndPdf())
                .expectErrorMatches(error -> {
                    assertEquals("Not Found Exception (404). Article(875675567)", error.getMessage());
                    return true;
                })
                .verify();
    }


    @Test
    void testUpdateInvoice() {
        Optional<Ticket> ticketOptional = ticketRepository.findById("201901121");
        ticketOptional.ifPresent(ticket -> {
            Shopping[] shoppingList = ticket.getShoppingList();
            shoppingList[0].setAmount(1);
            ticket.setShoppingList(shoppingList);
            ticketRepository.save(ticket);
        });

        StepVerifier
                .create(this.invoiceController.updateAndPdf("20201"))
                .expectNextMatches(invoice -> {
                    assertNotNull(invoice);
                    assertTrue(invoice.length > 0);
                    return true;
                })
                .expectComplete()
                .verify();

        StepVerifier
                .create(invoiceReactRepository.findById("20201"))
                .expectNextMatches(invoice -> {
                    assertNotNull(invoice.getCreationDate());
                    assertNotNull(invoice.getTicket());
                    assertNotNull(invoice.getUser());
                    assertEquals(new BigDecimal("14.2200"), invoice.getBaseTax());
                    assertEquals(new BigDecimal("3.7800"), invoice.getTax());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testUpdateInvoiceNotFound() {
        StepVerifier
                .create(this.invoiceController.updateAndPdf("12234"))
                .expectErrorMatches(throwable ->{
                    assertEquals("Not Found Exception (404). Invoice(12234)", throwable.getMessage());
                    return true;
                })
                .verify();
    }

    @Test
    void testReadQuarterlyVat() {
        StepVerifier
                .create(this.invoiceController.readQuarterlyVat(Quarter.Q2))
                .expectComplete()
                .verify();
    }

}
