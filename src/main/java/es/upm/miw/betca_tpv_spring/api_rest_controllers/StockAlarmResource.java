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
    public static final String STOCK_ALARMS_ID = "/{stockAlarmId}";
    public static final String STOCK_ALARMS_WARNING = "/warning";
    public static final String STOCK_ALARMS_CRITICAL = "/critical";

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

    @PutMapping(value = STOCK_ALARMS_ID)
    public Mono<StockAlarmOutputDto> updateStockAlarm(@PathVariable String stockAlarmId, @RequestBody StockAlarmInputDto stockAlarmInputDto){
        return this.stockAlarmController.updateStockAlarm(stockAlarmId,stockAlarmInputDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @DeleteMapping(value = STOCK_ALARMS_ID)
    public Mono<Void> deleteStockAlarm(@PathVariable String stockAlarmId) {
        return this.stockAlarmController.deleteStockAlarm(stockAlarmId)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = STOCK_ALARMS_WARNING)
    public Flux<StockAlarmOutputDto> searchWarning() {
        String warning = "warning";
        return this.stockAlarmController.searchWarning(warning)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = STOCK_ALARMS_CRITICAL)
    public Flux<StockAlarmOutputDto> searchCritical() {
        String critical = "critical";
        return this.stockAlarmController.searchWarning(critical)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
