package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class ArticleRepositoryIT {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void testGetLastArticle() {
        String codigo = this.articleRepository.findFirstByOrderByCodeDesc().getCode();
        assertEquals("8400000000086",codigo);
    }
}
