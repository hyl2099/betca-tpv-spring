package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.Article;

public class OrderLineDto {

    private Article article;
    private Integer requiredAmount;
    private Integer finalAmount;

    public OrderLineDto() {
        // empty for framework
    }

    public OrderLineDto(Article article, Integer requiredAmount) {
        this.article = article;
        this.requiredAmount = requiredAmount;
    }

    public OrderLineDto(Article article, Integer requiredAmount, Integer finalAmount) {
        this.article = article;
        this.requiredAmount = requiredAmount;
        this.finalAmount = finalAmount;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
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
