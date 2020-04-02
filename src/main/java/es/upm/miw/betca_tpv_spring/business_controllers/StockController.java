package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Article;
import es.upm.miw.betca_tpv_spring.dtos.ArticleStockDto;
import es.upm.miw.betca_tpv_spring.dtos.ShoppingDto;
import es.upm.miw.betca_tpv_spring.repositories.ArticleReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.TicketReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Controller
public class StockController {
    private ArticleReactRepository articleReactRepository;
    private TicketReactRepository ticketReactRepository;

    @Autowired
    public StockController(ArticleReactRepository articleReactRepository, TicketReactRepository ticketReactRepository) {
        this.articleReactRepository = articleReactRepository;
        this.ticketReactRepository = ticketReactRepository;
    }

    public Flux<ArticleStockDto> readAll(Integer minimumStock, LocalDateTime initDate, LocalDateTime endDate) {
        Flux<ArticleStockDto> articleSoldUnits = this.getSoldUnits(initDate, endDate);
        Flux<ArticleStockDto> articleInfo = this.getArticleInfo(minimumStock);
        return Flux.merge(articleSoldUnits, articleInfo).groupBy(ArticleStockDto::getCode)
                .flatMap(idFlux -> idFlux.reduce((article, article2) -> {
                            ArticleStockDto articleStockDto = article.getStock() != null ? article : article2;
                            articleStockDto.setSoldUnits(article.getSoldUnits() != null ? article.getSoldUnits() : article2.getSoldUnits());
                            return articleStockDto;
                        })
                ).map(article -> {
                    if (article.getSoldUnits() == null) {
                        article.setSoldUnits(0);
                    }
                    return article;
                });
    }

    public Flux<ArticleStockDto> getArticleInfo(Integer minimumStock) {
        Flux<Article> articlesFlux = minimumStock == null ? this.articleReactRepository.findAll()
                : this.articleReactRepository.findByStockLessThanEqual(minimumStock);
        return articlesFlux.map(article -> {
            ArticleStockDto articleStockDto = new ArticleStockDto();
            articleStockDto.setCode(article.getCode());
            articleStockDto.setStock(article.getStock());
            articleStockDto.setDescription(article.getDescription());
            return articleStockDto;
        });
    }

    public Flux<ArticleStockDto> getSoldUnits(LocalDateTime initDate, LocalDateTime endDate) {
        return this.getShopping().groupBy(ShoppingDto::getCode)
                .flatMap(idFlux -> idFlux.map(shopping -> {
                            ArticleStockDto article = new ArticleStockDto();
                            article.setCode(shopping.getCode());
                            article.setSoldUnits(shopping.getAmount());
                            return article;
                        }).reduce((article, article2) -> {
                            article.setSoldUnits(article.getSoldUnits() + article2.getSoldUnits());
                            return article;
                        })
                );
    }

    public Flux<ShoppingDto> getShopping() {
        return this.ticketReactRepository.findAll()
                .filter(ticket -> ticket.getShoppingList() != null)
                .flatMap(ticket -> Flux.just(ticket.getShoppingList()))
                .map(ShoppingDto::new);
    }

}
