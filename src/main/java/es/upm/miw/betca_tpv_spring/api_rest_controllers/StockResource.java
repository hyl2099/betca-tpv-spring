package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.StockController;
import es.upm.miw.betca_tpv_spring.dtos.ArticleStockDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(StockResource.STOCK)
public class StockResource {
    public static final String STOCK = "/stock";

    private StockController stockController;

    @Autowired
    public StockResource(StockController stockController) {
        this.stockController = stockController;
    }

    @GetMapping
    public Flux<ArticleStockDto> readAll() {
        return this.stockController.readAll()
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
