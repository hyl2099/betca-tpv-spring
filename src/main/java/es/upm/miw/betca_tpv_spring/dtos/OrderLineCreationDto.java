package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.Article;

public class OrderLineCreationDto {

    private String articleId;
    private Integer requiredAmount;

    public OrderLineCreationDto() {
        // for framework
    }

    public OrderLineCreationDto(String articleId, Integer requiredAmount) {
        this.articleId = articleId;
        this.requiredAmount = requiredAmount;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(Integer requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    @Override
    public String toString() {
        return "OrderLineCreationDto{" +
                "article=" + articleId +
                ", requiredAmount=" + requiredAmount +
                '}';
    }
}
