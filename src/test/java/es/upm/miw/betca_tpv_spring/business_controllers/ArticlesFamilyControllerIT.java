package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.documents.FamilyType;
import es.upm.miw.betca_tpv_spring.dtos.FamilyCompleteDto;
import es.upm.miw.betca_tpv_spring.dtos.ProviderDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class ArticlesFamilyControllerIT {

    private FamilyType familyType;

    @Autowired
    private ArticlesFamilyController articlesFamilyController;

    @Autowired
    private ProviderController providerController;

    @Test
    void testReadFamilyCompositeArticlesList() {
        assertNotNull(articlesFamilyController.readFamilyCompositeArticlesList("root"));
        assertEquals("varios", articlesFamilyController.readFamilyCompositeArticlesList("root").get(1).getDescription());
        assertEquals(FamilyType.values()[1], articlesFamilyController.readFamilyCompositeArticlesList("root").get(0).getFamilyType());
        assertEquals("descrip-a6", articlesFamilyController.readFamilyCompositeArticlesList("varios").get(1).getDescription());
        assertEquals(FamilyType.values()[0], articlesFamilyController.readFamilyCompositeArticlesList("varios").get(0).getFamilyType());
    }

    @Test
    void TestCreateFamilyArticle() throws IOException {
        assertNotNull(articlesFamilyController.readSizes());
        Flux<ProviderDto> provider = providerController.readAll();
        FamilyCompleteDto familyCompleteDto = FamilyCompleteDto.builder()
                                              .description("Jeans").sizeType(false).reference("Zaara")
                                              .fromSize("0").toSize("40").increment(2).provider(provider.collectList().block().get(0).getId()).build();
        assertNotNull(articlesFamilyController.createArticleFamily(familyCompleteDto));

    }

}
