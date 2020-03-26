package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Size;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SizeRepository extends MongoRepository<Size, String> {
    List<Size> findAll();
}
