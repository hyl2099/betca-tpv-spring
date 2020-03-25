package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.SizeType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SizeTypeRepository extends MongoRepository<SizeType, String> {
}
