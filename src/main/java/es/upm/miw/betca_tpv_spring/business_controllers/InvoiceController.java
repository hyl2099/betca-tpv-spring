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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

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
        Mono<Ticket> ticket = ticketReactRepository.findFirstByOrderByCreationDateDescIdDesc()
                .switchIfEmpty(Mono.error(new NotFoundException("Last Ticket not found")))
                .map(ticket1 -> {
                    invoice.setTicket(ticket1);
                    invoice.setUser(ticket1.getUser());
                    return ticket1;
                })
                .handle((ticket1, synchronousSink) -> {
                    User user = ticket1.getUser();
                    if (ticket1.getUser() == null || !isUserCompletedForInvoice(user))
                        synchronousSink.error(new BadRequestException("User not completed"));
                    else if (ticket1.isDebt())
                        synchronousSink.error(new BadRequestException("Ticket is debt"));
                    else
                        synchronousSink.complete();
                });

        Mono<Integer> nextId = this.nextIdStartingYearly()
                .map(Id -> {
                    invoice.setId(Id);
                    return Id;
                });
        return Mono.when(ticket, nextId).then(calculateBaseAndTax(invoice)).then(invoiceReactRepository.save(invoice));
    }

    private boolean isUserCompletedForInvoice(User user){
        return (user.getUsername() != null && !user.getUsername().trim().equals(""))
                && (user.getAddress() != null && !user.getAddress().trim().equals(""))
                && (user.getDni() != null && !user.getDni().trim().equals(""));
    }

    private Mono<Invoice> calculateBaseAndTax(Invoice invoice) {
        Flux<Article> articlesFlux = Flux.empty();
        Shopping[] ticketShoppingList = Arrays.stream(invoice.getTicket().getShoppingList())
                .filter(shopping -> shopping.getAmount() >= 1)
                .toArray(Shopping[]::new);
        for (Shopping shopping : ticketShoppingList) {
            Mono<Article> articleReact = this.articleReactRepository.findById(shopping.getArticleId())
                    .switchIfEmpty(Mono.error(new NotFoundException("Article(" + shopping.getArticleId() + ")")))
                    .doOnNext(article -> {
                        BigDecimal articleTaxRate = BigDecimal.ONE.divide(article.getTax().getRate(), RoundingMode.HALF_UP);
                        BigDecimal articleTax = shopping.getShoppingTotal().multiply(articleTaxRate);
                        invoice.setTax(invoice.getTax().add(articleTax));
                        BigDecimal articleBaseTax = shopping.getShoppingTotal().subtract(articleTax);
                        invoice.setBaseTax(invoice.getBaseTax().add(articleBaseTax));
                    });
            articlesFlux = Flux.merge(articleReact);
        }
        return articlesFlux.then(Mono.just(invoice));
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
