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


    public Flux<Staff> findByMobileOrYearOrMonth(Staff staff) {
        return this.staffReactRepository
                .findByMobileOrYearOrMonth(
                        staff.getMobile(),
                        staff.getYear(),
                        staff.getMonth()
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

    public Flux<Staff> findByMobileAndYearAndMonth(Staff staff) {
        return this.staffReactRepository
                .findByMobileAndYearAndMonth(
                        staff.getMobile(),
                        staff.getYear(),
                        staff.getMonth()
                );
    }

    public Flux<Staff> findByMobileAndYear(Staff staff) {
        return this.staffReactRepository
                .findByMobileAndYear(
                        staff.getMobile(),
                        staff.getYear()
                );
    }


    public Flux<Staff> findByMobileAndMonth(Staff staff) {
        return this.staffReactRepository
                .findByMobileAndMonth(
                        staff.getMobile(),
                        staff.getMonth()
                );
    }

    public Flux<Staff> findByYearAndMonth(Staff staff) {
        return this.staffReactRepository
                .findByYearAndMonth(
                        staff.getYear(),
                        staff.getMonth()
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

    public Mono<Staff> update(String id, StaffDto staffDto) {
        Mono<Staff> provider = this.staffReactRepository.findById(id).
                switchIfEmpty(Mono.error(new NotFoundException("Staff id mobile year month day " +
                        staffDto.getId() + staffDto.getMobile() + staffDto.getYear() + staffDto.getMonth() + staffDto.getDay())))
                .map(newStaff -> {
                    newStaff.setWorkHours(staffDto.getWorkHours());
                    newStaff.setLastLoginTime(staffDto.getLastLoginTime());
                    return newStaff;
                });

        return Mono.
                when(provider).
                then(this.staffReactRepository.saveAll(provider).next().map(Staff::new));
    }


}
