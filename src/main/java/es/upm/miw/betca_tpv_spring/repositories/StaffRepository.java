package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StaffRepository extends MongoRepository<Staff, String> {

}
