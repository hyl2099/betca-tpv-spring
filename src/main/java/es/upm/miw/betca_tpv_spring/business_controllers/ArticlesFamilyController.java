package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Article;
import es.upm.miw.betca_tpv_spring.documents.ArticlesFamily;
import es.upm.miw.betca_tpv_spring.documents.FamilyComposite;
import es.upm.miw.betca_tpv_spring.documents.FamilyType;
import es.upm.miw.betca_tpv_spring.dtos.ArticleFamilyCompleteDto;
import es.upm.miw.betca_tpv_spring.dtos.ArticlesFamilyDto;
import es.upm.miw.betca_tpv_spring.dtos.FamilyCompositeDto;
import es.upm.miw.betca_tpv_spring.repositories.ArticleRepository;
import es.upm.miw.betca_tpv_spring.repositories.ArticlesFamilyReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.FamilyCompositeReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.FamilyCompositeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ArticlesFamilyController {

    @Autowired
    private ArticlesFamilyReactRepository articlesFamilyReactRepository;

    @Autowired
    private FamilyCompositeReactRepository familyCompositeReactRepository;

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    @Autowired
    private ArticleRepository articleRepository;

//    public Mono<FamilyCompositeDto> readFamilyCompositeArticlesList(String description) {
//        return this.familyCompositeReactRepository.findByReference(description)
//                .map(FamilyCompositeDto::new);
//    }



    public List<ArticleFamilyCompleteDto> readFamilyCompositeArticlesList(String description) {
        FamilyComposite familyComplete = familyCompositeRepository.findFirstByDescription(description);
        List<ArticleFamilyCompleteDto> dtos = new ArrayList<>();
        System.out.println("description: " + description);
        System.out.println("familyCompleteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        System.out.println(familyComplete);
        System.out.println("articles family listttttt");
        System.out.println(familyComplete.getArticlesFamilyList());

        if (familyComplete.getFamilyType() == FamilyType.ARTICLES) {
            System.out.println("goes for articles");
            for (ArticlesFamily articlesFamily : familyComplete.getArticlesFamilyList()) {
                if (articlesFamily.getFamilyType() == FamilyType.ARTICLES) {
                    dtos.add(new ArticleFamilyCompleteDto(articlesFamily.getFamilyType(), articlesFamily.getDescription(), articlesFamily.getArticlesFamilyList()));
                }
                if (articlesFamily.getFamilyType() == FamilyType.ARTICLE) {
                    Article article = articleRepository.findByCode(articlesFamily.getArticleIdList().get(0));
                    System.out.println("ARTICLE");
                    System.out.println(article);
                    dtos.add(new ArticleFamilyCompleteDto(articlesFamily.getFamilyType(), article.getCode(), article.getDescription(), article.getRetailPrice()));
                }
            }
            System.out.println(dtos);
        }


        return dtos;

    }


    public List<ArticlesFamilyDto> readArticlesFamilyList(String reference) {
        FamilyComposite family = familyCompositeRepository.findFirstByReference(reference);
        List<ArticlesFamilyDto> dtos = new ArrayList<>();
        for (ArticlesFamily articlesFamily : family.getArticlesFamilyList()) {
            dtos.add(new ArticlesFamilyDto(articlesFamily));
        }
        return dtos;
    }
}
