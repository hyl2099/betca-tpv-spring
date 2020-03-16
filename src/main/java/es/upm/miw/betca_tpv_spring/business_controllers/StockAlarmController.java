package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.dtos.StockAlarmInputDto;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
import es.upm.miw.betca_tpv_spring.repositories.StockAlarmReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class StockAlarmController {

    private StockAlarmReactRepository stockAlarmReactRepository;

    @Autowired
    public StockAlarmController(StockAlarmReactRepository stockAlarmReactRepository) {
        this.stockAlarmReactRepository = stockAlarmReactRepository;
    }

    public Flux<StockAlarmInputDto> readAll(){
        return this.stockAlarmReactRepository.findAll()
                .switchIfEmpty(Flux.error(new BadRequestException("Bad Request")))
                .map(StockAlarmInputDto::new);
    }
}
