package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.dtos.validations.ListNotEmpty;
import es.upm.miw.betca_tpv_spring.dtos.validations.StringNotNullOrEmpty;

import java.util.List;

public class InvoiceNegativeCreationInputDto {

    public InvoiceNegativeCreationInputDto() {
    }

    public InvoiceNegativeCreationInputDto(String returnedTicketId, List<ShoppingDto> returnedShoppingList) {
        this.returnedTicketId = returnedTicketId;
        this.returnedShoppingList = returnedShoppingList;
    }

    @StringNotNullOrEmpty
    private String returnedTicketId;

    @ListNotEmpty
    private List<ShoppingDto> returnedShoppingList;

    public String getReturnedTicketId() {
        return returnedTicketId;
    }

    public List<ShoppingDto> getReturnedShoppingList() {
        return returnedShoppingList;
    }

    @Override
    public String toString() {
        return "InvoiceNegativeCreationInputDto{" +
                "returnedTicketId='" + returnedTicketId + '\'' +
                ", returnedShoppingList=" + returnedShoppingList +
                '}';
    }
}
