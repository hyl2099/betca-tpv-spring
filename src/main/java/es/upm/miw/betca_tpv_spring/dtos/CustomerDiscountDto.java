package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.betca_tpv_spring.documents.CustomerDiscount;
import es.upm.miw.betca_tpv_spring.documents.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerDiscountDto {

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime registrationDate;

    private BigDecimal discount;

    private BigDecimal minimumPurchase;

    private User user;

    private String mobile;

    public CustomerDiscountDto() {
        // Empty for framework
    }

    public CustomerDiscountDto(String description, LocalDateTime registrationDate, BigDecimal discount,
                               BigDecimal minimumPurchase, User user, String mobile) {
        this.description = description;
        this.registrationDate = LocalDateTime.now();
        this.discount = discount;
        this.minimumPurchase = minimumPurchase;
        this.user = user;
        this.mobile = mobile;
    }

    public CustomerDiscountDto(CustomerDiscount customerDiscount) {
        this.description = customerDiscount.getDescription();
        this.registrationDate = customerDiscount.getRegistrationDate();
        this.discount = customerDiscount.getDiscount();
        this.minimumPurchase = customerDiscount.getMinimumPurchase();
        this.user = customerDiscount.getUser();
        this.mobile = getMobile();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getMinimumPurchase() {
        return minimumPurchase;
    }

    public void setMinimumPurchase(BigDecimal minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) { this.user = user; }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public String toString() {
        return "CustomerDiscountDto [description=" + description + ", registrationDate=" + registrationDate +
                ", discount=" + discount + ", minimumPurchase=" + minimumPurchase + ", user=" + user + 
                ", mobile=" + mobile + "]";
    }
}

