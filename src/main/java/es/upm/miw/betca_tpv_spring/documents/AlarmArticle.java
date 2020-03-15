package es.upm.miw.betca_tpv_spring.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AlarmArticle {

    @Id
    private String articleId;
    private Integer warning;
    private Integer critical;

    public AlarmArticle() {
    }

    public AlarmArticle(String articleId, Integer warning, Integer critical) {
        this.articleId = articleId;
        this.warning = warning;
        this.critical = critical;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getWarning() {
        return warning;
    }

    public void setWarning(Integer warning) {
        this.warning = warning;
    }

    public Integer getCritical() {
        return critical;
    }

    public void setCritical(Integer critical) {
        this.critical = critical;
    }

    @Override
    public String toString() {
        return "AlarmArticle{" +
                "articleId='" + articleId + '\'' +
                ", warning=" + warning +
                ", critical=" + critical +
                '}';
    }
}
