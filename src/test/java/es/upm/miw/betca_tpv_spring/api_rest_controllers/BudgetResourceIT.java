package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.documents.Budget;
import es.upm.miw.betca_tpv_spring.documents.Shopping;
import es.upm.miw.betca_tpv_spring.documents.ShoppingState;
import es.upm.miw.betca_tpv_spring.dtos.BudgetCreationInputDto;
import es.upm.miw.betca_tpv_spring.dtos.BudgetDto;
import es.upm.miw.betca_tpv_spring.dtos.ShoppingDto;
import es.upm.miw.betca_tpv_spring.repositories.BudgetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Stream;

@ApiTestConfig
class BudgetResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private BudgetRepository budgetRepository;

    private LinkedList<Budget> listBudget;

    @BeforeEach
    void seedDataBudget() {
        listBudget = new LinkedList<>();
        Stream.iterate(0, i -> i + 1).limit(10)
                .map(i -> new Budget(new Shopping[]{
                        new Shopping(1, new BigDecimal("0"), ShoppingState.COMMITTED,
                                "8400000000017", "Zarzuela - Falda T2",   new BigDecimal("20")),
                        new Shopping(3, new BigDecimal("50"), ShoppingState.NOT_COMMITTED,
                                "8400000000024", "Zarzuela - Falda T4",   new BigDecimal("27.8"))
                }))
                .forEach(listBudget::add);
        this.budgetRepository.saveAll(listBudget);
    }

    @Test
    void testCreateBudget() {
        ShoppingDto shoppingDto =
                new ShoppingDto("3", "test", new BigDecimal("20.00"), 5, BigDecimal.ZERO,
                        new BigDecimal("200.00"), true);
        BudgetCreationInputDto budgetCreationInputDto = new BudgetCreationInputDto(Collections.singletonList(shoppingDto));
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + BudgetResource.BUDGETS)
                .body(BodyInserters.fromObject(budgetCreationInputDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void TestCreateBudgetWithoutShoppingCart() {
        ShoppingDto shoppingDto = null;
        BudgetCreationInputDto budgetCreationInputDto = new BudgetCreationInputDto(Collections.singletonList(shoppingDto));

        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + BudgetResource.BUDGETS)
                .body(BodyInserters.fromObject(budgetCreationInputDto))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testReadBudget() {
        this.restService.loginAdmin(this.webTestClient)
                .get().uri(contextPath + BudgetResource.BUDGETS  + BudgetResource.CODE_ID, this.listBudget.get(1).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BudgetDto.class)
                .returnResult().getResponseBody();
    }
}
