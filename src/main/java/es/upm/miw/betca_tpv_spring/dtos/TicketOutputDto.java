package es.upm.miw.betca_tpv_spring.dtos;

import javax.validation.constraints.NotNull;

public class TicketOutputDto {

    @NotNull
    private String id;

    @NotNull
    private String reference;

    public TicketOutputDto(String id, String reference) {
        this.id = id;
        this.reference = reference;
    }

    public String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "TicketOutputDto [id=" + id + ", reference=" + reference + "]";
    }
}
