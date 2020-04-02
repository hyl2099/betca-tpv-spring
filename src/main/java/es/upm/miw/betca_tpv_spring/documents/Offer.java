package es.upm.miw.betca_tpv_spring.documents;

import org.springframework.data.annotation.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Offer {
    @Id
    private String id;
    private LocalDateTime registrationDate;
    private LocalDateTime expirationDate;
    private String description;
    private BigDecimal discount;
    private Article[] articleList;

    public Offer() {
        this.registrationDate = LocalDateTime.now();
        this.id = new Encode().generateUUIDUrlSafe();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public Article[] getArticleList() {
        return articleList;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void setArticleList(Article[] articleList) {
        this.articleList = articleList;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && id.equals(((Offer) obj).id);
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id='" + id + '\'' +
                ", registrationDate=" + registrationDate +
                ", expirationDate=" + expirationDate +
                ", description='" + description + '\'' +
                ", discount=" + discount +
                ", articleList=" + Arrays.toString(articleList) +
                '}';
    }
}
