package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.documents.Staff;
import es.upm.miw.betca_tpv_spring.dtos.StaffDto;
import es.upm.miw.betca_tpv_spring.repositories.StaffReactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static es.upm.miw.betca_tpv_spring.api_rest_controllers.StaffResource.STAFFS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ApiTestConfig
public class StaffResourceIT {
    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private StaffReactRepository staffReactRepository;
    private LinkedList<Staff> listStaff;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @BeforeEach
    void seedDatabase() {
        listStaff = new LinkedList<>();
        listStaff.add(new Staff(
                "6662",
                "2020",
                "3",
                "13",
                4.00f,
                LocalDateTime.of(2020, 3, 13, 9, 0, 0)
        ));
        listStaff.add(new Staff(
                "6669",
                "2020",
                "3",
                "14",
                4.00f,
                LocalDateTime.of(2020, 3, 14, 9, 0, 0)
        ));
        this.staffReactRepository.saveAll(listStaff);

    }


    @Test
    void createStaffRecordTest() {
        this.restService.loginAdmin(this.webTestClient)
                .post().uri(contextPath + STAFFS)
                .body(BodyInserters.fromObject(listStaff.get(1)))
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void createStaffRecordTest2() {
        this.restService.loginAdmin(this.webTestClient)
                .post().uri(contextPath + STAFFS)
                .body(BodyInserters.fromObject(new Staff("6669",
                        "2020",
                        "3",
                        "14",
                        4.00f,
                        LocalDateTime.of(2020, 3, 14, 9, 0, 0))))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testReadAll() {
        this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "")
                        .queryParam("year", "")
                        .queryParam("month", "")
                        .queryParam("day", "")
                        .build()
                )
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void testReadByMobileYearMonthDay() {
        this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(contextPath + STAFFS + '?'
                        + "mobile=" + "6661" + "&year=" + "2020" + "&month=" + "3" + "&day=" + "13"
                )
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void testReadByMobileYearMonthDay2() {
        List<StaffDto> newStaffList = this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", listStaff.get(0).getMobile())
                        .queryParam("year", listStaff.get(0).getYear())
                        .queryParam("month", listStaff.get(0).getMonth())
                        .queryParam("day", listStaff.get(0).getDay())
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StaffDto.class)
                .returnResult().getResponseBody();


        assertNotNull(newStaffList);
        assertEquals(listStaff.get(0).getMobile(), newStaffList.get(0).getMobile());
    }

    @Test
    void testReadByMobileYearMonth() {
        List<StaffDto> newStaffList = this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "6661")
                        .queryParam("year", "2020")
                        .queryParam("month", "3")
                        .queryParam("day", "")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StaffDto.class)
                .returnResult().getResponseBody();

        assertNotNull(newStaffList);
        assertEquals("3", newStaffList.get(0).getMonth());
    }


    @Test
    void testReadByMobileMonth() {
        List<StaffDto> newStaffList = this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "6661")
                        .queryParam("year", "")
                        .queryParam("month", "3")
                        .queryParam("day", "")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StaffDto.class)
                .returnResult().getResponseBody();

        assertNotNull(newStaffList);
        assertEquals("3", newStaffList.get(0).getMonth());
    }

    @Test
    void testReadByMobileYear() {
        List<StaffDto> newStaffList = this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "6661")
                        .queryParam("year", "2020")
                        .queryParam("month", "")
                        .queryParam("day", "")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StaffDto.class)
                .returnResult().getResponseBody();

        assertNotNull(newStaffList);
        assertEquals("2020", newStaffList.get(0).getYear());
    }

    @Test
    void testReadByMobile() {
        List<StaffDto> newStaffList = this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "6661")
                        .queryParam("year", "")
                        .queryParam("month", "")
                        .queryParam("day", "")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StaffDto.class)
                .returnResult().getResponseBody();

        assertNotNull(newStaffList);
        assertEquals("6661", newStaffList.get(0).getMobile());
    }

    @Test
    void testReadByYear() {
        List<StaffDto> newStaffList = this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "")
                        .queryParam("year", "2020")
                        .queryParam("month", "")
                        .queryParam("day", "")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StaffDto.class)
                .returnResult().getResponseBody();

        assertNotNull(newStaffList);
        assertEquals("2020", newStaffList.get(0).getYear());
    }


    @Test
    void testReadByYearMonth() {
        List<StaffDto> newStaffList = this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "")
                        .queryParam("year", "2020")
                        .queryParam("month", "3")
                        .queryParam("day", "")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StaffDto.class)
                .returnResult().getResponseBody();

        assertNotNull(newStaffList);
        assertEquals("2020", newStaffList.get(0).getYear());
    }


    @Test
    void testReadByMonth() {
        List<StaffDto> newStaffList = this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "")
                        .queryParam("year", "")
                        .queryParam("month", "3")
                        .queryParam("day", "")
                        .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StaffDto.class)
                .returnResult().getResponseBody();

        assertNotNull(newStaffList);
        assertEquals("3", newStaffList.get(0).getMonth());
    }

    @Test
    void testUpdate() {
        this.restService.loginAdmin(this.webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(contextPath + STAFFS)
                        .queryParam("mobile", "6661")
                        .queryParam("year", "2020")
                        .queryParam("month", "3")
                        .queryParam("day", "13")
                        .build()
                )
                .exchange()
                .expectStatus().isOk();
    }

}
