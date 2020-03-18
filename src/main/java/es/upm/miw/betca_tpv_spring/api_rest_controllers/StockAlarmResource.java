package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.StockAlarmController;
import es.upm.miw.betca_tpv_spring.dtos.StockAlarmInputDto;
import es.upm.miw.betca_tpv_spring.dtos.StockAlarmOutputDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(StockAlarmResource.STOCK_ALARMS)

public class StockAlarmResource {

    public static final String STOCK_ALARMS = "/stock-alarms";
    private StockAlarmController stockAlarmController;

    @Autowired
    public StockAlarmResource(StockAlarmController stockAlarmController) {
        this.stockAlarmController = stockAlarmController;
    }

    @GetMapping
    public Flux<StockAlarmOutputDto> readAll() {
        return this.stockAlarmController.readAll()
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PostMapping
    public Mono<StockAlarmOutputDto> createStockAlarm(@RequestBody StockAlarmInputDto stockAlarmInputDto){
        return this.stockAlarmController.createStockAlarm(stockAlarmInputDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
