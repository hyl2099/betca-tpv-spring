package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.dtos.InvoiceFilterDto;
import es.upm.miw.betca_tpv_spring.dtos.InvoiceNegativeCreationInputDto;
import es.upm.miw.betca_tpv_spring.dtos.InvoiceOutputDto;
import es.upm.miw.betca_tpv_spring.dtos.ShoppingDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiTestConfig
public class InvoiceResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Test
    void testCreate() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + InvoiceResource.INVOICES)
                .exchange()
                .expectStatus().isOk()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void testGenerate() {
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + InvoiceResource.INVOICES + "/20201" + InvoiceResource.PRINT)
                .exchange()
                .expectStatus().isOk()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
    }


    @Test
    void testCreateInvoiceNegative() {
        List<ShoppingDto> shoppings = new ArrayList<>();
        shoppings.add(new ShoppingDto("8400000000055", "descrip-a5", new BigDecimal("0.23"),
                -2, new BigDecimal("50"), new BigDecimal("0.23"), true));
        InvoiceNegativeCreationInputDto invoiceNegativeCreationInputDto = new InvoiceNegativeCreationInputDto("201901125", shoppings);

        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + InvoiceResource.INVOICES + InvoiceResource.NEGATIVE)
                .body(BodyInserters.fromObject(invoiceNegativeCreationInputDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void testCreateInvoiceNegativeShoppingNotValid() {
        List<ShoppingDto> shoppings = new ArrayList<>();
        InvoiceNegativeCreationInputDto invoiceNegativeCreationInputDto = new InvoiceNegativeCreationInputDto("201901125", shoppings);

        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + InvoiceResource.INVOICES + InvoiceResource.NEGATIVE)
                .body(BodyInserters.fromObject(invoiceNegativeCreationInputDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void testCreateInvoiceNegativeTicketIdNotValid() {
        List<ShoppingDto> shoppings = new ArrayList<>();
        InvoiceNegativeCreationInputDto invoiceNegativeCreationInputDto = new InvoiceNegativeCreationInputDto("", shoppings);
        shoppings.add(new ShoppingDto("8400000000055", "descrip-a5", new BigDecimal("0.23"),
                -2, new BigDecimal("50"), new BigDecimal("0.23"), true));
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + InvoiceResource.INVOICES + InvoiceResource.NEGATIVE)
                .body(BodyInserters.fromObject(invoiceNegativeCreationInputDto))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void cget() {
        this.restService.loginAdmin(this.webTestClient)
        .get().uri(contextPath + InvoiceResource.INVOICES)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(InvoiceOutputDto.class)
                .value(Assertions::assertNotNull)
                .value(list -> assertTrue(list.size() > 1));
    }

    @Test
    void search() {
        this.restService.loginAdmin(this.webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + InvoiceResource.INVOICES + InvoiceResource.SEARCH)
                .queryParam("mobile", null)
                .queryParam("fromDate", LocalDateTime.now().minusDays(1).toLocalDate().format(DateTimeFormatter.ISO_DATE))
                .queryParam("toDate", LocalDateTime.now().plusDays(1).toLocalDate().format(DateTimeFormatter.ISO_DATE))
                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(InvoiceOutputDto.class)
                .value(Assertions::assertNotNull)
                .value(list -> assertTrue(list.size() > 1));
    }
}
