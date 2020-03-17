package es.upm.miw.betca_tpv_spring.dtos;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CashMovementInputDto {

    @NotNull
    private BigDecimal cashMovement;

    @NotNull
    private String comment;

    public CashMovementInputDto() {
        this(BigDecimal.ZERO, "");
    }

    public CashMovementInputDto(BigDecimal cashMovement, String comment) {
        this.cashMovement = cashMovement;
        this.comment = comment;
    }

    public BigDecimal getCashMovement() {
        return cashMovement;
    }

    public void setCashMovement(BigDecimal cashMovement) {
        this.cashMovement = cashMovement;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CashMovementInputDto{" +
                "cashMovement=" + this.cashMovement +
                ", comment='" + comment + '\'' +
                '}';
    }
}
