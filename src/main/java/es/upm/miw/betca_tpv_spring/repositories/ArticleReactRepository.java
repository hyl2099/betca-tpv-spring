package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Article;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ArticleReactRepository extends ReactiveSortingRepository<Article, String> {
    Flux<Article> findByDescriptionLikeOrProvider(String description, String provider);

    Flux<Article> findByCodeIn(List<String> codes);
}
