package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.business_services.PdfService;
import es.upm.miw.betca_tpv_spring.documents.Article;
import es.upm.miw.betca_tpv_spring.documents.Budget;
import es.upm.miw.betca_tpv_spring.documents.Shopping;
import es.upm.miw.betca_tpv_spring.documents.ShoppingState;
import es.upm.miw.betca_tpv_spring.dtos.BudgetCreationInputDto;
import es.upm.miw.betca_tpv_spring.dtos.BudgetDto;
import es.upm.miw.betca_tpv_spring.dtos.ShoppingDto;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
import es.upm.miw.betca_tpv_spring.repositories.ArticleReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.ArticleRepository;
import es.upm.miw.betca_tpv_spring.repositories.BudgetReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;

@Controller
public class BudgetController {

    private PdfService pdfService;
    private BudgetReactRepository budgetReactRepository;
    private ArticleReactRepository articleReactRepository;
    private ArticleRepository articleRepository;
    @Autowired
    public BudgetController(BudgetReactRepository budgetReactRepository, PdfService pdfService, ArticleReactRepository articleReactRepository, ArticleRepository articleRepository) {
        this.budgetReactRepository = budgetReactRepository;
        this.pdfService = pdfService;
        this.articleRepository = articleRepository;
    }

    public Mono<BudgetDto> readBudget(String code) {
        Mono<BudgetDto> b= this.budgetReactRepository.findById(code).
                switchIfEmpty(Mono.error(new NotFoundException("Budget code (" + code + ")")))
                .map(BudgetDto::new);
        return Mono.when(b).then(b.doOnNext(budget -> { BudgetDto budgetDto = budget;
                    for (ShoppingDto shDto:budgetDto.getShoppingCart()) {
                        Article article = this.articleRepository.findById(shDto.getCode()).get();
                        if(article.getRetailPrice().compareTo(shDto.getRetailPrice()) ==-1 ){
                            shDto.setRetailPrice(article.getRetailPrice());
                            shDto.setDiscount(BigDecimal.ZERO);
                            shDto.setTotal(article.getRetailPrice().multiply(new BigDecimal(shDto.getAmount())));
                        }
                        else if(article.getRetailPrice().compareTo(shDto.getRetailPrice()) ==1 ){
                            BigDecimal percent = new BigDecimal("100").subtract((shDto.getTotal().divide((article.getRetailPrice().multiply(new BigDecimal(shDto.getAmount()))), MathContext.DECIMAL128).multiply(new BigDecimal("100"))));
                            BigDecimal newTotal = shDto.getTotal().subtract(shDto.getTotal().multiply(percent.divide(new BigDecimal("100"))));
                            if(shDto.getTotal().compareTo(newTotal) == 1){
                                shDto.setDiscount(percent);
                                shDto.setRetailPrice(article.getRetailPrice());
                            }
                        }
                    }
                }
        ));
    }

    public Mono<Budget> createBudget(BudgetCreationInputDto budgetCreationInputDto) {
        Shopping[] shoppingArray = budgetCreationInputDto.getShoppingCart().stream().map(shoppingDto ->
                new Shopping(shoppingDto.getAmount(), shoppingDto.getDiscount(),
                        shoppingDto.isCommitted() ? ShoppingState.COMMITTED : ShoppingState.NOT_COMMITTED,
                        shoppingDto.getCode(), shoppingDto.getDescription(), shoppingDto.getRetailPrice()))
                .toArray(Shopping[]::new);
        Budget budget = new Budget(shoppingArray);
        return this.budgetReactRepository.save(budget);
    }

    @Transactional
    public Mono<byte[]> createBudgetAndPdf(BudgetCreationInputDto budgetCreationInputDto) {
        return pdfService.generateBudget(this.createBudget(budgetCreationInputDto));
    }
}