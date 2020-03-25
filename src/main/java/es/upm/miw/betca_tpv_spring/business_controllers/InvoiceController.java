package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.business_services.PdfService;
import es.upm.miw.betca_tpv_spring.documents.*;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
import es.upm.miw.betca_tpv_spring.repositories.ArticleReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.InvoiceReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class InvoiceController {

    private PdfService pdfService;
    private InvoiceReactRepository invoiceReactRepository;
    private TicketReactRepository ticketReactRepository;
    private ArticleReactRepository articleReactRepository;

    @Autowired
    public InvoiceController(PdfService pdfService,
                             InvoiceReactRepository invoiceReactRepository,
                             TicketReactRepository ticketReactRepository,
                             ArticleReactRepository articleReactRepository) {
        this.pdfService = pdfService;
        this.invoiceReactRepository = invoiceReactRepository;
        this.ticketReactRepository = ticketReactRepository;
        this.articleReactRepository = articleReactRepository;
    }

    public Mono<Invoice> createInvoice() {
        Invoice invoice = new Invoice(0, null, null);
        Mono<Ticket> ticketPublisher = ticketReactRepository.findFirstByOrderByCreationDateDescIdDesc()
                .switchIfEmpty(Mono.error(new NotFoundException("Last Ticket not found")))
                .doOnNext(ticket1 -> {
                    invoice.setTicket(ticket1);
                    invoice.setUser(ticket1.getUser());
                    System.out.println("==== ticket >>>> " + ticket1);
                })
                .handle((ticket, synchronousSink) -> {
                    User user = ticket.getUser();
                    if (ticket.getUser() == null || !isUserCompletedForInvoice(user))
                        synchronousSink.error(new BadRequestException("User not completed"));
                    else if (ticket.isDebt())
                        synchronousSink.error(new BadRequestException("Ticket is debt"));
                    else
                        synchronousSink.next(ticket);
                });

        Mono<Integer> nextId = this.nextIdStartingYearly()
                .map(id -> {
                    invoice.setId(id);
                    System.out.println("==== id >>>> " + id);
                    return id;
                });
        Mono<Void> x = this.calculateBaseAndTax(invoice, ticketPublisher);
        return Mono.when(x, nextId)
                .then(invoiceReactRepository.save(invoice));
    }


    private boolean isUserCompletedForInvoice(User user) {
        return (user.getUsername() != null && !user.getUsername().trim().equals(""))
                && (user.getAddress() != null && !user.getAddress().trim().equals(""))
                && (user.getDni() != null && !user.getDni().trim().equals(""));
    }

    private Mono<Void> calculateBaseAndTax(Invoice invoice, Mono<Ticket> ticketPublisher) {
        return ticketPublisher
                .switchIfEmpty(Mono.error(new NotFoundException("Ticket")))
                .flatMap(ticket -> {
                    Stream<Shopping> ticketShoppingList = Arrays.stream(invoice.getTicket().getShoppingList())
                            .filter(shopping -> shopping.getAmount() >= 1);
                    List<Mono<Article>> articlePublishers = ticketShoppingList
                            .map(shopping -> this.articleReactRepository.findById(shopping.getArticleId())
                                    .switchIfEmpty(Mono.error(new NotFoundException("Article(" + shopping.getArticleId() + ")")))
                                    .doOnNext(article -> {
                                        BigDecimal articleTaxRate = BigDecimal.ONE.divide(article.getTax().getRate(), RoundingMode.HALF_UP);
                                        BigDecimal articleTax = shopping.getShoppingTotal().multiply(articleTaxRate);
                                        invoice.setTax(invoice.getTax().add(articleTax));
                                        BigDecimal articleBaseTax = shopping.getShoppingTotal().subtract(articleTax);
                                        invoice.setBaseTax(invoice.getBaseTax().add(articleBaseTax));
                                    })).collect(Collectors.toList());
                    return Mono.when(articlePublishers);
                });
    }


    private Mono<Integer> nextIdStartingYearly() {
        return invoiceReactRepository.findFirstByOrderByCreationDateDescIdDesc()
                .map(invoice -> {
                    if (invoice.getCreationDate().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN))) {
                        return invoice.simpleId() + 1;
                    } else {
                        return 1;
                    }
                })
                .switchIfEmpty(Mono.just(1));
    }

    @Transactional
    public Mono<Byte[]> createAndPdf() {
        return pdfService.generateInvoice(this.createInvoice());
    }
}
