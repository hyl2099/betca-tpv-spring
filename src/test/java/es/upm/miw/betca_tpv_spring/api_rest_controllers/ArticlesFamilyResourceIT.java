package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.documents.FamilyType;
import es.upm.miw.betca_tpv_spring.dtos.ArticlesFamilyDto;
import es.upm.miw.betca_tpv_spring.dtos.FamilyCompleteDto;
import es.upm.miw.betca_tpv_spring.dtos.ProviderCreationDto;
import es.upm.miw.betca_tpv_spring.dtos.ProviderDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static es.upm.miw.betca_tpv_spring.api_rest_controllers.ArticlesFamilyResource.ARTICLES_FAMILY;
import static es.upm.miw.betca_tpv_spring.api_rest_controllers.ArticlesFamilyResource.FAMILY_COMPOSITE;
import static es.upm.miw.betca_tpv_spring.api_rest_controllers.ProviderResource.PROVIDERS;
import static es.upm.miw.betca_tpv_spring.api_rest_controllers.ArticlesFamilyResource.SIZES;
import static org.junit.Assert.*;

@ApiTestConfig
public class ArticlesFamilyResourceIT {
    private FamilyType familyType;
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
/*
    @Test
    void testCreateArticleFamilySize() {
        ProviderCreationDto providerCreationDto =
                new ProviderCreationDto("pro4", "12345678J", "C/TPV-pro, 4", "9166666603", "p4@gmail.com", "p4", true);
        ProviderDto provider = this.restService.loginAdmin(this.webTestClient)
                .post().uri(contextPath + PROVIDERS)
                .body(BodyInserters.fromObject(providerCreationDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProviderDto.class)
                .returnResult().getResponseBody();
        assertNotNull(provider);
        String id = provider.getId();
        FamilyCompleteDto familyCompleteDto = new FamilyCompleteDto();
        familyCompleteDto.setDescription("Test Product");
        familyCompleteDto.setSizeType("1");
        familyCompleteDto.setRefence("Test Reference");
        familyCompleteDto.setFromSize("2");
        familyCompleteDto.setToSize("3");
        familyCompleteDto.setProvider(id);

        ArticlesFamilyDto articleFamily = this.restService.loginAdmin(this.webTestClient)
                .post().uri(contextPath + ARTICLES_FAMILY)
                .body(BodyInserters.fromObject(familyCompleteDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArticlesFamilyDto.class)
                .returnResult().getResponseBody();
        assertNotNull(articleFamily);
        assertEquals("Test Reference", articleFamily.getReference());
        assertEquals(null, articleFamily.getCode());
        assertEquals(FamilyType.values()[2], articleFamily.getFamilyType());


    }
    */


    @Test
    void testSize() {
        List<String> sizes = this.restService.loginAdmin(this.webTestClient)
                .get().uri(contextPath + ARTICLES_FAMILY +SIZES)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .returnResult().getResponseBody();
        assertNotNull(sizes);
        assertTrue(sizes.size()>0);
    }
}
