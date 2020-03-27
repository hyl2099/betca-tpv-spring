package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.*;
import es.upm.miw.betca_tpv_spring.dtos.ArticleFamilyCompleteDto;
import es.upm.miw.betca_tpv_spring.repositories.ArticleRepository;
import es.upm.miw.betca_tpv_spring.repositories.FamilyCompositeRepository;
import es.upm.miw.betca_tpv_spring.repositories.SizeRepository;
import es.upm.miw.betca_tpv_spring.repositories.SizeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ArticlesFamilyController {

    @Autowired
    private FamilyCompositeRepository familyCompositeRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SizeTypeRepository sizeTypeRepository;

    @Autowired
    private SizeRepository sizeRepository;

    public List<ArticleFamilyCompleteDto> readFamilyCompositeArticlesList(String description) {
        FamilyComposite familyComplete = familyCompositeRepository.findFirstByDescription(description);
        List<ArticleFamilyCompleteDto> dtos = new ArrayList<>();

        if (familyComplete.getFamilyType() == FamilyType.ARTICLES) {
            for (ArticlesFamily articlesFamily : familyComplete.getArticlesFamilyList()) {
                if (articlesFamily.getFamilyType() == FamilyType.ARTICLES) {
                    dtos.add(new ArticleFamilyCompleteDto(articlesFamily.getFamilyType(), articlesFamily.getDescription(), articlesFamily.getArticlesFamilyList()));
                }
                if (articlesFamily.getFamilyType() == FamilyType.ARTICLE) {
                    Article article = articleRepository.findByCode(articlesFamily.getArticleIdList().get(0));
                    dtos.add(new ArticleFamilyCompleteDto(articlesFamily.getFamilyType(), article.getCode(), article.getDescription(), article.getRetailPrice()));
                }
                if (articlesFamily.getFamilyType() == FamilyType.SIZES) {
                    dtos.add(new ArticleFamilyCompleteDto(articlesFamily.getFamilyType(), articlesFamily.getReference(), articlesFamily.getDescription()));
                }
            }
        } else if (familyComplete.getFamilyType() == FamilyType.SIZES) {
            for (ArticlesFamily articlesFamily : familyComplete.getArticlesFamilyList()) {
                Article article = articleRepository.findByCode(articlesFamily.getArticleIdList().get(0));
                dtos.add(new ArticleFamilyCompleteDto(article.getReference().split("T")[1], article.getStock(), article.getRetailPrice(), article.getCode()));
            }
        }
        return dtos;

    }

    public List<SizeType> readAllSizeTypes()
    {
        return sizeTypeRepository.findAll();
    }

    public List<Size> findSizeBySizeTypeId(String id)
    {
        return this.sizeRepository.findBySizeTypeIn(this.sizeTypeRepository.findById(id));
    }


}
