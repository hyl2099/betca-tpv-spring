package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Staff;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface StaffReactRepository extends ReactiveSortingRepository<Staff, String> {
    Flux<Staff> findByMobileOrYearOrMonth(String mobile, String year, String month);

    Flux<Staff> findByMobileAndYearAndMonthAndDay(String mobile, String year, String month, String day);

    Flux<Staff> findByMobileAndYearAndMonth(String mobile, String year, String month);

    Flux<Staff> findByMobileAndYear(String mobile, String year);

    Flux<Staff> findByMobileAndMonth(String mobile, String month);

    Flux<Staff> findByYearAndMonth(String year, String month);
}
