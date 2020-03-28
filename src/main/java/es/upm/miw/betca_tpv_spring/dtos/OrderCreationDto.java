package es.upm.miw.betca_tpv_spring.dtos;

import java.util.Arrays;

public class OrderCreationDto {

    private String description;

    private String providerId;

    private OrderLineCreationDto[] orderLines;

    public OrderCreationDto() {
    }

    public OrderCreationDto(String description, String providerId, OrderLineCreationDto[] orderLines) {
        this.description = description;
        this.providerId = providerId;
        this.orderLines = orderLines;
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

    public OrderLineCreationDto[] getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(OrderLineCreationDto[] orderLines) {
        this.orderLines = orderLines;
    }

    @Override
    public String toString() {
        return "OrderCreationDto{" +
                "description='" + description + '\'' +
                ", provider=" + providerId +
                ", orderLines=" + Arrays.toString(orderLines) +
                '}';
    }
}
