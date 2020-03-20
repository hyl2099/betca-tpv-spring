package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.documents.OrderLine;
import es.upm.miw.betca_tpv_spring.documents.Provider;

import java.time.LocalDateTime;
import java.util.Arrays;

public class OrderDto {

    private String id;

    private String description;

    private String provider;

    private LocalDateTime openingDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime closingDate;

    private OrderLineDto[] orderLines;

    public OrderDto(){
        // Empty for framework
    }

    public OrderDto(String id, String description, String provider, LocalDateTime openingDate, OrderLineDto[] orderLines) {
        this.id = id;
        this.description = description;
        this.provider = provider;
        this.openingDate = openingDate;
        this.orderLines = orderLines;
    }

    public OrderDto(String id, String description, String provider, LocalDateTime openingDate) {
        this.id = id;
        this.description = description;
        this.provider = provider;
        this.openingDate = openingDate;
    }

    public OrderDto(String description, String provider, LocalDateTime openingDate, OrderLineDto[] orderLines) {
        this.description = description;
        this.provider = provider;
        this.openingDate = openingDate;
        this.orderLines = orderLines;
    }

    public OrderDto(Order order) {
        this(order.getId(), order.getDescription(), order.getProvider().getId(), order.getOpeningDate());
        OrderLineDto[] orderLineDtos = new OrderLineDto[order.getOrderLines().length];
        for (int i = 0; i < order.getOrderLines().length; i++) {
            orderLineDtos[i] = new OrderLineDto(order.getOrderLines()[i].getArticle().getCode(), order.getOrderLines()[i].getRequiredAmount());
        }
        this.setOrderLines(orderLineDtos);
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
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
