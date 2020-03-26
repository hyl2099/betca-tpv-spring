package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.AlarmArticle;
import es.upm.miw.betca_tpv_spring.documents.Article;
import es.upm.miw.betca_tpv_spring.documents.StockAlarm;
import es.upm.miw.betca_tpv_spring.dtos.StockAlarmInputDto;
import es.upm.miw.betca_tpv_spring.dtos.StockAlarmOutputDto;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
import es.upm.miw.betca_tpv_spring.repositories.ArticleReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.StockAlarmReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Controller
public class StockAlarmController {

    private ArticleReactRepository articleReactRepository;
    private StockAlarmReactRepository stockAlarmReactRepository;

    @Autowired
    public StockAlarmController(ArticleReactRepository articleReactRepository, StockAlarmReactRepository stockAlarmReactRepository) {
        this.articleReactRepository = articleReactRepository;
        this.stockAlarmReactRepository = stockAlarmReactRepository;
    }

    public Flux<StockAlarmOutputDto> readAll() {
        return this.stockAlarmReactRepository.findAll()
                .switchIfEmpty(Flux.error(new BadRequestException("Bad Request")))
                .map(StockAlarmOutputDto::new);
    }

    public Mono<StockAlarmOutputDto> createStockAlarm(StockAlarmInputDto stockAlarmInputDto) {
        StockAlarm stockAlarm = new StockAlarm(
                stockAlarmInputDto.getDescription(),
                stockAlarmInputDto.getProvider(),
                stockAlarmInputDto.getWarning(),
                stockAlarmInputDto.getCritical(),
                stockAlarmInputDto.getAlarmArticle()
        );
        return this.stockAlarmReactRepository.save(stockAlarm)
                .map(StockAlarmOutputDto::new);
    }

    public Mono<StockAlarmOutputDto> updateStockAlarm(String stockAlarmId, StockAlarmInputDto stockAlarmInputDto) {
        Mono<StockAlarm> stockAlarm = this.stockAlarmReactRepository.findById(stockAlarmId)
                .switchIfEmpty(Mono.error(new NotFoundException("StockAlarm Id"+ stockAlarmId)))
                .map(stockAlarm1 -> {
                    stockAlarm1.setDescription(stockAlarmInputDto.getDescription());
                    stockAlarm1.setProvider(stockAlarmInputDto.getProvider());
                    stockAlarm1.setWarning(stockAlarmInputDto.getWarning());
                    stockAlarm1.setCritical(stockAlarmInputDto.getCritical());
                    stockAlarm1.setAlarmArticle(stockAlarmInputDto.getAlarmArticle());
                    return stockAlarm1;
                });
        return Mono
                .when(stockAlarm)
                .then(this.stockAlarmReactRepository.saveAll(stockAlarm).next().map(StockAlarmOutputDto::new));
    }

    public Mono<Void> deleteStockAlarm(String stockAlarmId) {
        Mono<StockAlarm> stockAlarm = this.stockAlarmReactRepository.findById(stockAlarmId);
        return Mono
                .when(stockAlarm)
                .then(this.stockAlarmReactRepository.deleteById(stockAlarmId));
    }

    public Flux<StockAlarmOutputDto> searchWarning(final String warningOrCritical) {
        Flux<StockAlarmOutputDto> stockAlarmFluxResults = this.stockAlarmReactRepository.findAll()
                .map(StockAlarmOutputDto::new);
        List<StockAlarmOutputDto> stockAlarmPoJoList = new ArrayList<>();
        stockAlarmFluxResults.toStream().forEach(stockAlarm -> {
            AlarmArticle[] alarmArticles = stockAlarm.getAlarmArticle();
            List<AlarmArticle>  alarmArticleList = new ArrayList<>();
            StockAlarmOutputDto stockAlarmPoJo = new StockAlarmOutputDto();
            for (AlarmArticle alarmArticle : alarmArticles) {
                Mono<Article> article = this.articleReactRepository.findById(alarmArticle.getArticleId());
                Flux<Article> concat = Flux.concat(article);
                concat.toStream().forEach(articlePojo -> {
                    if (warningOrCritical.equals("warning")){
                        if (articlePojo.getStock() < alarmArticle.getWarning()) {
                            alarmArticleList.add(alarmArticle);
                        }
                    }else if (warningOrCritical.equals("critical")){
                        if (articlePojo.getStock() < alarmArticle.getCritical()) {
                            alarmArticleList.add(alarmArticle);
                        }
                    }
                });
            }
            if (!alarmArticleList.isEmpty()) {
                stockAlarmPoJo.setAlarmArticle(alarmArticleList.toArray(new AlarmArticle[alarmArticleList.size()]));
                stockAlarmPoJo.setId(stockAlarm.getId());
                stockAlarmPoJo.setDescription(stockAlarm.getDescription());
                stockAlarmPoJo.setProvider(stockAlarm.getProvider());
                stockAlarmPoJo.setWarning(stockAlarm.getWarning());
                stockAlarmPoJo.setCritical(stockAlarm.getCritical());
                stockAlarmPoJoList.add(stockAlarmPoJo);
            }
        });
        return Flux.fromIterable(stockAlarmPoJoList);
    }
}
