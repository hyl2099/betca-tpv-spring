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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Mono<Ticket> ticket = ticketReactRepository.findFirstByOrderByCreationDateDescIdDesc()
                .switchIfEmpty(Mono.error(new NotFoundException("Last Ticket not found")))
                .doOnNext(ticket1 -> {
                    invoice.setTicket(ticket1);
                    invoice.setUser(ticket1.getUser());
                })
                .handle((ticket1, synchronousSink) -> {
                    User user = ticket1.getUser();
                    if (ticket1.getUser() == null || isUserCompletedForInvoice(user))
                        synchronousSink.error(new BadRequestException("User not completed"));
                    else if (ticket1.isDebt())
                        synchronousSink.error(new BadRequestException("Ticket is debt"));
                    else
                        synchronousSink.complete();
                });

        Mono<Integer> nextId = this.nextIdStartingYearly()
                .doOnNext(invoice::setId);
        return Mono.when(ticket, nextId).then(calculateBaseAndTax(invoice).then(invoiceReactRepository.save(invoice)));
    }

    private boolean isUserCompletedForInvoice(User user){
        return (user.getUsername() != null && user.getUsername().trim() != "")
                && (user.getAddress() != null && user.getAddress().trim() != "")
                && (user.getDni() != null && user.getDni().trim() != "");
    }

    private Mono<Invoice> calculateBaseAndTax(Invoice invoice) {
        Flux<Article> articlesFlux = Flux.empty(); // Esto representa el flujo  de articulos del ticket
        for (Shopping shopping : invoice.getTicket().getShoppingList()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(shopping.getArticleId())
                    .switchIfEmpty(Mono.error(new NotFoundException("Article(" + shopping.getArticleId() + ")")))
                    .doOnNext(article -> {
                        BigDecimal articleTaxRate = BigDecimal.ONE.divide(article.getTax().getRate(), BigDecimal.ROUND_UP);
                        BigDecimal articleTax = article.getRetailPrice().multiply(articleTaxRate);
                        invoice.setTax(invoice.getTax().add(articleTax));
                        BigDecimal articleBaseTax = article.getRetailPrice().subtract(articleTax);
                        invoice.setBaseTax(invoice.getBaseTax().add(articleBaseTax));
                    }); //aqui se modifica invoice y se le añade base & tax de éste artículo
            articlesFlux = Flux.merge(articleReact); // con esto creamos un flujo compuesto por todos los accesos a BBDD, nos sirve para sincronizar
        }
        return articlesFlux.then(Mono.just(invoice));  //Cuando se ha terminado todos los acceso a BBDD, creamos el mono de invoice ya actualizado con base & tax
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

    public Mono<Byte[]> createAndPdf() {
        return pdfService.generateInvoice(this.createInvoice());
    }
}
