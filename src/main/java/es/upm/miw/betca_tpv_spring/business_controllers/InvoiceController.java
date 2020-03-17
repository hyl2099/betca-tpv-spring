package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.business_services.PdfService;
import es.upm.miw.betca_tpv_spring.documents.Invoice;
import es.upm.miw.betca_tpv_spring.documents.Shopping;
import es.upm.miw.betca_tpv_spring.repositories.InvoiceReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class InvoiceController {

    private PdfService pdfService;
    private InvoiceReactRepository invoiceReactRepository;
    private TicketReactRepository ticketReactRepository;

    @Autowired
    public InvoiceController(PdfService pdfService,
                             InvoiceReactRepository invoiceReactRepository,
                             TicketReactRepository ticketReactRepository){
        this.pdfService = pdfService;
        this.invoiceReactRepository = invoiceReactRepository;
        this.ticketReactRepository = ticketReactRepository;
    }

    public Mono<Invoice> create(){
        ticketReactRepository.findFirstByOrderByCreationDateDescIdDesc()
                .map(ticket -> {
                    List<String> ArticleIds = Stream.of(ticket.getShoppingList())
                            .map(Shopping::getArticleId)
                            .collect(Collectors.toList());
                    //TODO GameEngineers mapping of ticket and generate invoice with taxes
                    return null;
                });
        return null;
    }

    public Mono<Byte[]> createAndPdf() {
        return pdfService.generateInvoice(this.create());
    }
}
