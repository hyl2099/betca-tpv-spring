package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Staff;
import es.upm.miw.betca_tpv_spring.dtos.StaffDto;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
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


    public Flux<Staff> findByMobileAndYearAndMonthAndDay(String mobile,String year, String month, String day) {
        return this.staffReactRepository.findByMobileAndYearAndMonthAndDay(mobile, year, month, day);
    }

    public Mono<Staff> createStaffRecord(StaffDto staffDto) {
        Staff staff = new Staff(
                staffDto.getId(),
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
