package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.CustomerPoints;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerPointsRepository extends MongoRepository<CustomerPoints, String> {
}
