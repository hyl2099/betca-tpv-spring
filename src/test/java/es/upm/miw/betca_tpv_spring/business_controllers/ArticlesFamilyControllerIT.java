package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.dtos.FamilyCompositeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
public class ArticlesFamilyControllerIT {

    @Autowired
    private ArticlesFamilyController articlesFamilyController;


    private FamilyCompositeDto familyCompositeDto;

    @BeforeEach
    void seed() {
        this.familyCompositeDto = new FamilyCompositeDto("root", null);
    }

    @Test
    void testReadFamilyCompositeArticlesList(){
        assertNotNull(articlesFamilyController.readArticlesFamilyList("root"));
    }

    @Test
    void testReadArticlesFamilyList() {
        assertNotNull(articlesFamilyController.readArticlesFamilyList("root"));
    }

}
