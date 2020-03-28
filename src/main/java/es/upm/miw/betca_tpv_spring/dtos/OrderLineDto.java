package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

public class OrderLineDto {

    private String articleId;

    private Integer requiredAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer finalAmount;

    public OrderLineDto() {
        // empty for framework
    }

    public OrderLineDto(String articleId, Integer requiredAmount) {
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

    public Integer getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Integer finalAmount) {
        this.finalAmount = finalAmount;
    }

    @Override
    public String toString() {
        return "OrderLineDto{" +
                "article=" + articleId +
                ", requiredAmount=" + requiredAmount +
                ", finalAmount=" + finalAmount +
                '}';
    }
}
