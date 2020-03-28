package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.InvoiceController;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(InvoiceResource.INVOICES)
public class InvoiceResource {

    public static final String INVOICES = "/invoices";

    private InvoiceController invoiceController;

    @Autowired
    public InvoiceResource(InvoiceController invoiceController){
        this.invoiceController = invoiceController;
    }

    @PostMapping
    public Mono<byte[]> create(){
        return this.invoiceController.createAndPdf()
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
