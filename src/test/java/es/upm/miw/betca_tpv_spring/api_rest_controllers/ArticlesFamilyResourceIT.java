package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;

import static es.upm.miw.betca_tpv_spring.api_rest_controllers.ArticlesFamilyResource.*;

@ApiTestConfig
public class ArticlesFamilyResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;


    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Test
    void testReadInFamilyCompositeVarios() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                .path(contextPath + ARTICLES_FAMILY + FAMILY_COMPOSITE)
                .queryParam("description", "root").build()
        ).exchange().expectStatus().isOk();
    }


    @Test
    void testReadInFamilyCompositeReturnsServerError() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                .path(contextPath + ARTICLES_FAMILY + FAMILY_COMPOSITE)
                .queryParam("description", "kk").build()
        ).exchange().expectStatus().is5xxServerError();
    }

    @Test
    void testReadInFamilyCompositeRootReturnsOk() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                .path(contextPath + ARTICLES_FAMILY + FAMILY_COMPOSITE)
                .queryParam("description", "root").build()
        ).exchange().expectStatus().isOk();
    }
}
