package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.Ticket;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class TicketOutputDto {

    @NotNull
    private String id;

    @NotNull
    private String reference;

    private ShoppingDto[] shoppingList;

    public TicketOutputDto(){
        //Empty for framework
    }

    public TicketOutputDto(String id, String reference) {
        this.id = id;
        this.reference = reference;
    }

    public TicketOutputDto(Ticket ticket) {
        ShoppingDto[] shoppingDtos = new ShoppingDto[ticket.getShoppingList().length];
        final int[] i = {0};
        Arrays.stream(ticket.getShoppingList()).forEach(e -> {
            shoppingDtos[i[0]] = new ShoppingDto(e);
            i[0]++;
        });
        id = ticket.getId();
        reference = ticket.getReference();
        shoppingList = shoppingDtos;
    }

    public String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public ShoppingDto[] getShoppingList() { return shoppingList; }

    @Override
    public String toString() {
        return "TicketOutputDto{" +
                "id='" + this.id + '\'' +
                ", reference='" + this.reference + '\'' +
                ", shoppingList=" + Arrays.toString(this.shoppingList) +
                '}';
    }
}
