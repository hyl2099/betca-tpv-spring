package es.upm.miw.betca_tpv_spring.repositories;

import es.upm.miw.betca_tpv_spring.documents.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Flux;
import java.util.stream.Stream;

public interface ArticleRepository extends MongoRepository<Article, String> {
    Flux<Article> findByCodeIn(Stream<String> codes);
}
