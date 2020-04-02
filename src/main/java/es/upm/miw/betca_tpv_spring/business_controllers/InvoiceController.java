package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.business_services.PdfService;
import es.upm.miw.betca_tpv_spring.documents.*;
import es.upm.miw.betca_tpv_spring.dtos.QuarterVATDto;
import es.upm.miw.betca_tpv_spring.dtos.TaxDto;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
import es.upm.miw.betca_tpv_spring.repositories.ArticleReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.InvoiceReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class InvoiceController {

    @Value("${miw.tax.general}")
    private Double generalTax;
    @Value("${miw.tax.reduced}")
    private Double reducedTax;
    @Value("${miw.tax.super.reduced}")
    private Double superReducedTax;

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

    private Mono<Invoice> createInvoice() {
        Invoice invoice = new Invoice(0, null, null);
        Mono<Ticket> ticketPublisher = ticketReactRepository.findFirstByOrderByCreationDateDescIdDesc()
                .switchIfEmpty(Mono.error(new NotFoundException("Last Ticket not found")))
                .doOnNext(ticket -> {
                    invoice.setTicket(ticket);
                    invoice.setUser(ticket.getUser());
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
                    return id;
                });
        Mono<Invoice> calculateBaseAndTaxPublisher = this.calculateBaseAndTax(invoice, ticketPublisher);
        return Mono.when(calculateBaseAndTaxPublisher, nextId)
                .then(invoiceReactRepository.save(invoice));
    }


    private boolean isUserCompletedForInvoice(User user) {
        return (user.getUsername() != null && !user.getUsername().trim().equals(""))
                && (user.getAddress() != null && !user.getAddress().trim().equals(""))
                && (user.getDni() != null && !user.getDni().trim().equals(""));
    }

    private Mono<Invoice> calculateBaseAndTax(Invoice invoice, Mono<Ticket> ticketPublisher) {
        return ticketPublisher.flatMap(ticket -> calculateBaseAndTax(invoice));
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
    public Mono<byte[]> createAndPdf() {
        return pdfService.generateInvoice(this.createInvoice());
    }

    @Transactional
    public Mono<byte[]> updateAndPdf(String id) {
        return pdfService.generateInvoice(this.updateInvoice(id));
    }

    private Mono<Invoice> updateInvoice(String id) {
        return invoiceReactRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Invoice(" + id + ")")))
                .flatMap(this::calculateBaseAndTax)
                .flatMap(invoice -> invoiceReactRepository.save(invoice));
    }

    private Mono<Invoice> calculateBaseAndTax(Invoice invoice) {
        Stream<Shopping> ticketShoppingList = Arrays.stream(invoice.getTicket().getShoppingList());
        List<Mono<Article>> articlePublishers = ticketShoppingList
                .map(shopping -> this.articleReactRepository.findById(shopping.getArticleId())
                        .switchIfEmpty(Mono.error(new NotFoundException("Article(" + shopping.getArticleId() + ")")))
                        .filter(article -> article.getTax() != Tax.FREE)
                        .doOnNext(article -> {
                            BigDecimal articleTaxRate = article.getTax().getRate().divide(new BigDecimal("100"));
                            BigDecimal articleTax = shopping.getShoppingTotal().multiply(articleTaxRate);
                            invoice.setTax(invoice.getTax().add(articleTax));
                            BigDecimal articleBaseTax = shopping.getShoppingTotal().subtract(articleTax);
                            invoice.setBaseTax(invoice.getBaseTax().add(articleBaseTax));
                        })).collect(Collectors.toList());
        return Mono.when(articlePublishers).then(Mono.just(invoice));
    }

    private BigDecimal vatFromTax(Tax tax) {
        if (tax.equals(Tax.SUPER_REDUCED)) {
            return BigDecimal.valueOf(this.superReducedTax);
        } else if (tax.equals(Tax.REDUCED)) {
            return BigDecimal.valueOf(this.reducedTax);
        } else if (tax.equals(Tax.GENERAL)) {
            return BigDecimal.valueOf(this.generalTax);
        } else return BigDecimal.ZERO;
    }

    private Flux<ShoppingLine> convertShoppingArrayToShoppingLineFlux(Shopping[] shoppings) {
        Flux<ShoppingLine> shoppingLineFlux = Flux.empty();
        for (Shopping shopping : shoppings) {
            Mono<ShoppingLine> taxDtoMono = this.articleReactRepository.findById(shopping.getArticleId())
                    .map(Article::getTax)
                    .map(tax -> new ShoppingLine(tax, this.vatFromTax(tax), shopping.getShoppingTotal()));
            shoppingLineFlux = shoppingLineFlux.mergeWith(taxDtoMono);
        }
        return shoppingLineFlux;
    }

    public Mono<QuarterVATDto> readQuarterlyVat(Quarter quarter) {
        System.out.println("======START----->>>>>");
        QuarterVATDto quarterVATDto = new QuarterVATDto(quarter);
        quarterVATDto.getTaxes().add(new TaxDto(Tax.GENERAL, generalTax));
        quarterVATDto.getTaxes().add(new TaxDto(Tax.REDUCED, reducedTax));
        quarterVATDto.getTaxes().add(new TaxDto(Tax.SUPER_REDUCED, superReducedTax));

        Flux<Shopping[]> shoppingFlux = this.invoiceReactRepository.findAll()
                .filter(invoice -> quarter.getQuarterFromDate(invoice.getCreationDate()).equals(quarter))
                .map(invoice -> {
                    return invoice.getTicket().getShoppingList();
                });

        Flux<ShoppingLine> shoppingLineFlux = shoppingFlux
                .flatMap(shoppings -> this.convertShoppingArrayToShoppingLineFlux(shoppings));

        shoppingLineFlux = shoppingLineFlux
                .doOnNext(shoppingLine -> {
                    //TODO Aqui se monta el DTO con los totales acumulados
                    System.out.println("====== ---- >>>>>: " + shoppingLine);
                });
/*

        Flux<Shopping[]> x = shoppingFlux.doOnEach(shoppings -> {
            Arrays.stream(shoppings.get()).
                    forEach(shopping -> {
                        Mono<TaxDto> taxDto = this.articleReactRepository.findById(shopping.getArticleId())
                                .map(Article::getTax)
                                .map(tax -> new TaxDto(tax, shopping.getShoppingTotal().doubleValue()));
                    })


                    .forEach(item -> {
                        BigDecimal totalAmount = item.getShoppingTotal();
                        System.out.println("totalAmount: " + totalAmount);
                        System.out.println("articleId: " + item.getArticleId());

                        Mono<Tax> map = this.articleReactRepository.findById(item.getArticleId())
                                .switchIfEmpty(Mono.error(new NotFoundException("Article " + item.getArticleId() + " not found")))
                                .map(Article::getTax)
                                .doOnNext(tax -> {
                                    System.out.println("Tax:" + tax);
                                    if (tax.compareTo(Tax.GENERAL) == 0) {
                                        quarterVATDto.getTaxes().get(0).setTaxableAmount(quarterVATDto.getTaxes().get(0).getTaxableAmount().add(totalAmount.divide(new BigDecimal(generalTax).add(new BigDecimal(1)))));
                                        quarterVATDto.getTaxes().get(0).setVat(quarterVATDto.getTaxes().get(0).getVat().add(totalAmount.subtract(totalAmount.divide(new BigDecimal(generalTax).add(new BigDecimal(1))))));
                                        System.out.println("generalTax:" + quarterVATDto.getTaxes().get(0));
                                    } else if (tax.compareTo(Tax.REDUCED) == 0) {
                                        quarterVATDto.getTaxes().get(1).setTaxableAmount(quarterVATDto.getTaxes().get(1).getTaxableAmount().add(totalAmount.divide(new BigDecimal(reducedTax).add(new BigDecimal(1)))));
                                        quarterVATDto.getTaxes().get(1).setVat(quarterVATDto.getTaxes().get(1).getVat().add(totalAmount.subtract(totalAmount.divide(new BigDecimal(reducedTax).add(new BigDecimal(1))))));
                                    } else if (tax.compareTo(Tax.SUPER_REDUCED) == 0) {
                                        quarterVATDto.getTaxes().get(2).setTaxableAmount(quarterVATDto.getTaxes().get(2).getTaxableAmount().add(totalAmount.divide(new BigDecimal(superReducedTax).add(new BigDecimal(1)))));
                                        quarterVATDto.getTaxes().get(2).setVat(quarterVATDto.getTaxes().get(2).getVat().add(totalAmount.subtract(totalAmount.divide(new BigDecimal(superReducedTax).add(new BigDecimal(1))))));
                                    }
                                });
                    });
        });*/
        return Mono.when(shoppingLineFlux).then(Mono.empty()); //TODO se devuelve un mono con el DTO
    }
}
