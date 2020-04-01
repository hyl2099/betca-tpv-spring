package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static es.upm.miw.betca_tpv_spring.api_rest_controllers.CustomerPointsResource.CUSTOMER_POINTS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ApiTestConfig
public class CustomerPointsResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Test
    void readCustomerPointsByExistingUserMobileTest() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + CUSTOMER_POINTS + CustomerPointsResource.MOBILE_ID, "666666001")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .value(Assertions::assertNotNull)
                .value(points -> assertEquals(20, points.intValue()));
    }

    @Test
    void readCustomerPointsByUserMobileWithNullValueTest() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + CUSTOMER_POINTS + CustomerPointsResource.MOBILE_ID, this.restService.getAdminMobile())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateCustomerPointsByUserMobileTest() {
        this.restService.loginAdmin(this.webTestClient)
                .put().uri(contextPath + CUSTOMER_POINTS + CustomerPointsResource.MOBILE_ID, "666666004")
                .body(BodyInserters.fromObject(new Integer(1000)))
                .exchange().expectStatus().isOk();
    }

    @Test
    void updateCustomerPointsByUserMobileNotFoundTest() {
        this.restService.loginAdmin(this.webTestClient)
                .put().uri(contextPath + CUSTOMER_POINTS + CustomerPointsResource.MOBILE_ID, "696666004")
                .body(BodyInserters.fromObject(new Integer(1000)))
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void createCustomerPointsByExistingUserMobileTest() {
        this.restService.loginAdmin(this.webTestClient)
                .post().uri(contextPath + CUSTOMER_POINTS)
                .body(BodyInserters.fromObject(
                        "666666005"
                )).exchange().expectStatus().isOk().expectBody(String.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void createCustomerPointsByUserMobileNotFoundTest() {
        this.restService.loginAdmin(this.webTestClient)
                .post().uri(contextPath + CUSTOMER_POINTS)
                .body(BodyInserters.fromObject(
                        "66666600a5"
                )).exchange().expectStatus().isNotFound();
    }
}
