package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Staff;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface StaffReactRepository extends ReactiveSortingRepository<Staff, String> {
    Flux<Staff> findByMobileOrYearOrMonthOrDay(String mobile, String year, String month, String day);

    Flux<Staff> findByMobileAndYearAndMonthAndDay(String mobile, String year, String month, String day);
}
