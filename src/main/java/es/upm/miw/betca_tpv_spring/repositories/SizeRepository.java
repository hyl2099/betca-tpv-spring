package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Size;
import es.upm.miw.betca_tpv_spring.documents.SizeType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SizeRepository extends MongoRepository<Size, String> {
    List<Size> findAll();
    List<Size> findBySizeTypeIn(Optional<SizeType> sizeType);
}
