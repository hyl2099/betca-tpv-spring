package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Staff;
import es.upm.miw.betca_tpv_spring.dtos.StaffDto;
import es.upm.miw.betca_tpv_spring.repositories.StaffReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class StaffController {
    private StaffReactRepository staffReactRepository;

    @Autowired
    public StaffController(StaffReactRepository staffReactRepository){
        this.staffReactRepository = staffReactRepository;
    }

    public Flux<Staff> readAll() {
        return this.staffReactRepository.findAll();
    }


    public Flux<Staff> findByMobileOrYearOrMonthOrDay(Staff staff) {
        return this.staffReactRepository
                .findByMobileOrYearOrMonthOrDay(
                        staff.getMobile(),
                        staff.getYear(),
                        staff.getMonth(),
                        staff.getDay()
                        );
    }

    public Flux<Staff> findByMobileAndYearAndMonthAndDay(Staff staff) {
        return this.staffReactRepository
                .findByMobileAndYearAndMonthAndDay(
                        staff.getMobile(),
                        staff.getYear(),
                        staff.getMonth(),
                        staff.getDay()
                );
    }

    public Mono<Staff> createStaffRecord(StaffDto staffDto) {
        Staff staff = new Staff(
                staffDto.getMobile(),
                staffDto.getYear(),
                staffDto.getMonth(),
                staffDto.getDay(),
                staffDto.getWorkHours(),
                staffDto.getLastLoginTime()
        );
        return staffReactRepository.save(staff);
    }


}
