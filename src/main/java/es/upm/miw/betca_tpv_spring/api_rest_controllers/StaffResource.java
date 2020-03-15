package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.StaffController;
import es.upm.miw.betca_tpv_spring.documents.Staff;
import es.upm.miw.betca_tpv_spring.dtos.StaffDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(StaffResource.STAFFS)
public class StaffResource {

    public static final String STAFFS = "/staffs";
    public static final String ID = "/{id}";
    public static final String MOBILE = "/{mobile}";
    public static final String YEAR = "/{year}";
    public static final String MONTH = "/{month}";
    public static final String DAY = "/{day}";

    private StaffController staffController;

    @Autowired
    public StaffResource(StaffController staffController){
        this.staffController = staffController;
    }

    @GetMapping
    public Flux<Staff> findByMobileAndYearAndMonthAndDay(@RequestParam  String mobile,
                                                 @RequestParam String year,
                                                 @RequestParam String month,
                                                 @RequestParam String day) {
        Staff staff = new Staff(mobile, year, month, day);
        if ((mobile == null || mobile.equals("") || mobile.equals("null")) &&
                (year == null || year.equals("") || year.equals("null")) &&
                (month == null || month.equals("") || month.equals("null")) &&
                (day == null || day.equals("") || day.equals("null"))) {
            return this.staffController.readAll()
                    .doOnEach(log -> LogManager.getLogger(this.getClass()).debug(log));
        } else if ((mobile != null && !mobile.equals("") && !mobile.equals("null")) &&
                (year != null && !year.equals("") && !year.equals("null")) &&
                (month != null && !month.equals("") && !month.equals("null")) &&
                (day != null && !day.equals("") && !day.equals("null"))) {
            return this.staffController.findByMobileAndYearAndMonthAndDay(staff)
                    .doOnEach(log -> LogManager.getLogger(this.getClass()).debug(log));
        } else {
            return this.staffController.findByMobileOrYearOrMonthOrDay(staff)
                    .doOnEach(log -> LogManager.getLogger(this.getClass()).debug(log));
        }
    }

    @PostMapping(produces = {"application/json"})
    public Mono<Staff> createStaffRecord(@Valid @RequestBody StaffDto staffDto){
        return this.staffController.createStaffRecord(staffDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

//    @PutMapping(value = ID, produces = {"application/json"})
//    public Mono<Staff> update(@PathVariable String id,
////                              @PathVariable String year,
////                              @PathVariable String month,
////                              @PathVariable String day,
//                              @Valid @RequestBody StaffDto staffDto) {
//        return this.staffController.update(id, staffDto)
//                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
//    }

    @PutMapping(value = ID, produces = {"application/json"})
    public Mono<Staff> update(@PathVariable String id,@Valid @RequestBody StaffDto staffDto) {
        return this.staffController.update(id,staffDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

}
