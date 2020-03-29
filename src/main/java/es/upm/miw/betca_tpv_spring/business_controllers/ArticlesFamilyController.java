package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.business_services.Barcode;
import es.upm.miw.betca_tpv_spring.documents.*;
import es.upm.miw.betca_tpv_spring.dtos.ArticleFamilyCompleteDto;
import es.upm.miw.betca_tpv_spring.dtos.ArticlesFamilyDto;
import es.upm.miw.betca_tpv_spring.dtos.FamilyCompleteDto;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
import es.upm.miw.betca_tpv_spring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    @Autowired
    private ArticlesFamilyReactRepository articlesFamilyReactRepository;

    @Autowired
    private ProviderRepository providerRepository;

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

    public List<SizeType> readAllSizeTypes() {
        return sizeTypeRepository.findAll();
    }

    public List<Size> findSizeBySizeTypeId(String id) {
        return this.sizeRepository.findBySizeTypeIn(this.sizeTypeRepository.findById(id));
    }

    public Mono<ArticlesFamilyDto> createArticleFamily(FamilyCompleteDto articlesFamilyDto) {
        List<Size> sizes = this.sizeRepository.findByIdBetween((articlesFamilyDto.getFromSize()), articlesFamilyDto.getToSize());
        List<ArticlesFamily> familyArticleList = new ArrayList<>();
        Optional<Provider> provider = this.providerRepository.findById(articlesFamilyDto.getProvider());
        for (int index = 0; index < sizes.size() - 1; index = index++ + articlesFamilyDto.getIncrement()) {
            String code = new Barcode().generateEan13code(Long.parseLong(this.articleRepository.findFirstByOrderByCodeDesc().getCode().substring(0, 12)) + 1);
            if (code.length() == 13 && Long.parseLong(code.substring(7, 12)) > 99999L) {
                return Mono.error(new BadRequestException("Index out of range"));
            }
            Article article = Article.builder(code).description(articlesFamilyDto.getDescription() + " T" + sizes.get(index).getDescription())
                    .reference(articlesFamilyDto.getRefence()).provider(provider.get()).build();
            this.articleRepository.save(article);
            familyArticleList.add(new FamilyArticle(article));
        }
        ArticlesFamily familyCompositeSizesList = new FamilyComposite(FamilyType.SIZES, articlesFamilyDto.getRefence(), articlesFamilyDto.getDescription());
        for (ArticlesFamily articlesFamily : familyArticleList) {
            familyCompositeSizesList.add(articlesFamily);
        }
        return this.articlesFamilyReactRepository.save(familyCompositeSizesList).map(ArticlesFamilyDto::new);
    }


}
