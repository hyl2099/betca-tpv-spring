package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.StockAlarm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockAlarmRepository extends MongoRepository<StockAlarm, String> {

}
