package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

public class OrderSearchDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String providerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String closingDate;

    public OrderSearchDto() {
        //empty for framework
    }

    public OrderSearchDto(String description, String providerId, String closingDate) {
        this.description = description;
        this.providerId = providerId;
        this.closingDate = closingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    @Override
    public String toString() {
        return "OrderSearchDto{" +
                "description='" + description + '\'' +
                ", provider='" + providerId + '\'' +
                ", closingDate=" + closingDate +
                '}';
    }
}
