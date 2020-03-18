package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.StockAlarm;
import es.upm.miw.betca_tpv_spring.dtos.StockAlarmInputDto;
import es.upm.miw.betca_tpv_spring.dtos.StockAlarmOutputDto;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
import es.upm.miw.betca_tpv_spring.repositories.StockAlarmReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class StockAlarmController {

    private StockAlarmReactRepository stockAlarmReactRepository;

    @Autowired
    public StockAlarmController(StockAlarmReactRepository stockAlarmReactRepository) {
        this.stockAlarmReactRepository = stockAlarmReactRepository;
    }

    public Flux<StockAlarmOutputDto> readAll() {
        return this.stockAlarmReactRepository.findAll()
                .switchIfEmpty(Flux.error(new BadRequestException("Bad Request")))
                .map(StockAlarmOutputDto::new);
    }

    public Mono<StockAlarmOutputDto> createStockAlarm(StockAlarmInputDto stockAlarmInputDto) {
        StockAlarm stockAlarm = new StockAlarm(
                stockAlarmInputDto.getDescription(),
                stockAlarmInputDto.getProvider(),
                stockAlarmInputDto.getWarning(),
                stockAlarmInputDto.getCritical(),
                stockAlarmInputDto.getAlarmArticle()
        );
        return this.stockAlarmReactRepository.save(stockAlarm)
                .map(StockAlarmOutputDto::new);
    }
}
