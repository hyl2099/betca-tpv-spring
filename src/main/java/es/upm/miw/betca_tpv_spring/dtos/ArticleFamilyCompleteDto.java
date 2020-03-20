package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.ArticlesFamily;
import es.upm.miw.betca_tpv_spring.documents.FamilyType;

import java.math.BigDecimal;
import java.util.List;

public class ArticleFamilyCompleteDto {

    private String description;

    private String reference;

    private Integer stock;

    private String size;

    private String code;

    private BigDecimal retailPrice;

    private FamilyType familyType;

    private List<ArticlesFamily> articlesFamilyList;

    public ArticleFamilyCompleteDto() {
    }

    public ArticleFamilyCompleteDto(FamilyType familyType, String description, List<ArticlesFamily> articlesFamilyList) {
    }

    public ArticleFamilyCompleteDto(FamilyType familyType, String code, String description, BigDecimal retailPrice) {
    }

    public ArticleFamilyCompleteDto(FamilyType familyType, String reference, String description) {
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public FamilyType getFamilyType() {
        return familyType;
    }

    public void setFamilyType(FamilyType familyType) {
        this.familyType = familyType;
    }

    public List<ArticlesFamily> getArticlesFamilyList() {
        return articlesFamilyList;
    }

    public void setArticlesFamilyList(List<ArticlesFamily> articlesFamilyList) {
        this.articlesFamilyList = articlesFamilyList;
    }

    @Override
    public String toString() {
        return "ArticleFamilyCompleteDto{" +
                "description='" + description + '\'' +
                ", reference='" + reference + '\'' +
                ", stock=" + stock +
                ", size='" + size + '\'' +
                ", code='" + code + '\'' +
                ", retailPrice=" + retailPrice +
                ", familyType=" + familyType +
                ", articlesFamilyList=" + articlesFamilyList +
                '}';
    }
}

