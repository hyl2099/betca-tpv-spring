package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.StockAlarm;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface StockAlarmReactRepository extends ReactiveSortingRepository<StockAlarm, String> {
}
