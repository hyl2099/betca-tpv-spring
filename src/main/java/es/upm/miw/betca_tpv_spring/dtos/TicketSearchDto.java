package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public class TicketSearchDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mobile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer amount;

    public TicketSearchDto(String mobile, LocalDateTime date, Integer amount) {
        this.mobile = mobile;
        this.date = date;
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TicketSearchDto{" +
                "mobile='" + mobile + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}