package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.SizeType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface SizeTypeRepository extends MongoRepository<SizeType, String> {

    List<SizeType> findAll();
    SizeType findById();
}
