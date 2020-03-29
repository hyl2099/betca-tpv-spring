package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.documents.FamilyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class ArticlesFamilyControllerIT {

    private FamilyType familyType;

    @Autowired
    private ArticlesFamilyController articlesFamilyController;

    @Test
    void testReadFamilyCompositeArticlesList() {
        assertNotNull(articlesFamilyController.readFamilyCompositeArticlesList("root"));
        assertEquals("varios", articlesFamilyController.readFamilyCompositeArticlesList("root").get(1).getDescription());
        assertEquals(FamilyType.values()[1], articlesFamilyController.readFamilyCompositeArticlesList("root").get(0).getFamilyType());
        assertEquals("descrip-a6", articlesFamilyController.readFamilyCompositeArticlesList("varios").get(1).getDescription());
        assertEquals(FamilyType.values()[0], articlesFamilyController.readFamilyCompositeArticlesList("varios").get(0).getFamilyType());
    }

}
