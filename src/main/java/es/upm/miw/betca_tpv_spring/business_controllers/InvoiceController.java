package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.business_services.PdfService;
import es.upm.miw.betca_tpv_spring.documents.Article;
import es.upm.miw.betca_tpv_spring.documents.Invoice;
import es.upm.miw.betca_tpv_spring.documents.Shopping;
import es.upm.miw.betca_tpv_spring.documents.Ticket;
import es.upm.miw.betca_tpv_spring.repositories.ArticleReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.InvoiceReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<Invoice> create() {
        Mono<Invoice> ticketMono = ticketReactRepository.findFirstByOrderByCreationDateDescIdDesc()
                .map(t -> {
                    List<String> articleCodes = Stream.of(t.getShoppingList())
                            .map(Shopping::getArticleId)
                            .collect(Collectors.toList());
//                    articleReactRepository.findByCodeIn(articleCodes)
//                            .reduce(article -> {
//
//                            });
                    return null;
                });

        //TODO GameEngineers mapping of ticket and generate invoice with taxes

        return null;
    }

    public Mono<Byte[]> createAndPdf() {
        return pdfService.generateInvoice(this.create());
    }
}
