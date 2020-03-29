package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.Sendings;

public class SendingsDto {

    private String id;
    private Boolean estado;

    public SendingsDto() {
    }

    public SendingsDto(String id, Boolean estado) {
        this.id = id;
        this.estado = estado;
    }

    public SendingsDto(Sendings sendings) {
        this(sendings.getId(), sendings.getEstado());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "SendingsDto{" +
                "id='" + id + '\'' +
                ", estado=" + estado +
                '}';
    }
}
