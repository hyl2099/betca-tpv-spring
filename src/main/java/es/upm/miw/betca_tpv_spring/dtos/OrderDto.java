package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.betca_tpv_spring.documents.OrderLine;
import es.upm.miw.betca_tpv_spring.documents.Provider;

import java.time.LocalDateTime;
import java.util.Arrays;

public class OrderDto {

    private String id;

    private String description;

    private Provider provider;

    private LocalDateTime openingDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime closingDate;

    private OrderLineDto[] orderLines;

    public OrderDto(){
        // Empty for framework
    }

    public OrderDto(String description, Provider provider, LocalDateTime openingDate, OrderLineDto[] orderLines) {
        this.description = description;
        this.provider = provider;
        this.openingDate = openingDate;
        this.orderLines = orderLines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDateTime closingDate) {
        this.closingDate = closingDate;
    }

    public OrderLineDto[] getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(OrderLineDto[] orderLines) {
        this.orderLines = orderLines;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", provider=" + provider +
                ", openingDate=" + openingDate +
                ", closingDate=" + closingDate +
                ", orderLines=" + Arrays.toString(orderLines) +
                '}';
    }
}
