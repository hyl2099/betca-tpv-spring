package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

public class OrderLineDto {

    private String article;

    private Integer requiredAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer finalAmount;

    public OrderLineDto() {
        // empty for framework
    }

    public OrderLineDto(String article, Integer requiredAmount) {
        this.article = article;
        this.requiredAmount = requiredAmount;
    }

    public OrderLineDto(String article, Integer requiredAmount, Integer finalAmount) {
        this.article = article;
        this.requiredAmount = requiredAmount;
        this.finalAmount = finalAmount;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
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
                "article=" + article +
                ", requiredAmount=" + requiredAmount +
                ", finalAmount=" + finalAmount +
                '}';
    }
}
