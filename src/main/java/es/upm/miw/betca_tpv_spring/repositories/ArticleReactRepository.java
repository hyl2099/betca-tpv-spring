package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Article;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface ArticleReactRepository extends ReactiveSortingRepository<Article, String> {
    Flux<Article> findByDescriptionContainingOrProvider(String description, String provider);
}
