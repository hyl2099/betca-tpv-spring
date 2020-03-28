package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.ArticlesFamilyController;
import es.upm.miw.betca_tpv_spring.documents.Size;
import es.upm.miw.betca_tpv_spring.documents.SizeType;
import es.upm.miw.betca_tpv_spring.dtos.ArticleFamilyCompleteDto;
import es.upm.miw.betca_tpv_spring.dtos.ArticlesFamilyDto;
import es.upm.miw.betca_tpv_spring.dtos.FamilyCompleteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RequestMapping(ArticlesFamilyResource.ARTICLES_FAMILY)
@RestController
public class ArticlesFamilyResource {

    public static final String ARTICLES_FAMILY = "/articles-family";
    public static final String FAMILY_COMPOSITE = "/familydescription";
    public static final String SIZES_TYPE = "/sizes-type";
    public static final String SIZES = "/sizes";
    @Autowired
    private ArticlesFamilyController articlesFamilyController;

    @GetMapping(value = FAMILY_COMPOSITE)
    public List<ArticleFamilyCompleteDto> readInFamilyComposite(@Valid @RequestParam String description){
        return articlesFamilyController.readFamilyCompositeArticlesList(description);
    }

    @GetMapping(value = SIZES_TYPE)
    public List<SizeType> readAllSizeType() {
        return articlesFamilyController.readAllSizeTypes();
    }

    @GetMapping(value = SIZES + "/{id}")
    public List<Size> findSizeBySizeTypeId(@PathVariable String id) {
        return articlesFamilyController.findSizeBySizeTypeId(id);
    }


    @PostMapping
    public Mono<ArticlesFamilyDto> createArticleFamily(@Valid @RequestBody FamilyCompleteDto articleFamilyDto)
    {
        return articlesFamilyController.createArticleFamily(articleFamilyDto);
    }

}
