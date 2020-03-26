package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.CashierClosure;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CashierClosureSearchDto {
    private LocalDateTime openingDate;
    private BigDecimal initialCash;
    private BigDecimal salesCard;
    private BigDecimal salesCash;
    private BigDecimal usedVouchers;
    private BigDecimal deposit;
    private BigDecimal withdrawal;
    private BigDecimal lostCard;
    private BigDecimal lostCash;
    private BigDecimal finalCash;
    private String comment;
    private LocalDateTime closureDate;

    public CashierClosureSearchDto(CashierClosure cashierClosure){
        this.setOpeningDate(cashierClosure.getOpeningDate());
        this.setInitialCash(cashierClosure.getInitialCash());
        this.setSalesCard(cashierClosure.getSalesCard());
        this.setSalesCash(cashierClosure.getSalesCash());
        this.setUsedVouchers(cashierClosure.getUsedVouchers());
        this.setDeposit(cashierClosure.getDeposit());
        this.setWithdrawal(cashierClosure.getWithdrawal());
        this.setLostCash(cashierClosure.getLostCash());
        this.setLostCard(cashierClosure.getLostCard());
        this.setFinalCash(cashierClosure.getFinalCash());
        this.setComment(cashierClosure.getComment());
        this.setClosureDate(cashierClosure.getClosureDate());
    }

    public CashierClosureSearchDto() {

    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public BigDecimal getInitialCash() {
        return initialCash;
    }

    public void setInitialCash(BigDecimal initialCash) {
        this.initialCash = initialCash;
    }

    public BigDecimal getSalesCard() {
        return salesCard;
    }

    public void setSalesCard(BigDecimal salesCard) {
        this.salesCard = salesCard;
    }

    public BigDecimal getSalesCash() {
        return salesCash;
    }

    public void setSalesCash(BigDecimal salesCash) {
        this.salesCash = salesCash;
    }

    public BigDecimal getUsedVouchers() {
        return usedVouchers;
    }

    public void setUsedVouchers(BigDecimal usedVouchers) {
        this.usedVouchers = usedVouchers;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(BigDecimal withdrawal) {
        this.withdrawal = withdrawal;
    }

    public BigDecimal getLostCard() {
        return lostCard;
    }

    public void setLostCard(BigDecimal lostCard) {
        this.lostCard = lostCard;
    }

    public BigDecimal getLostCash() {
        return lostCash;
    }

    public void setLostCash(BigDecimal lostCash) {
        this.lostCash = lostCash;
    }

    public BigDecimal getFinalCash() {
        return finalCash;
    }

    public void setFinalCash(BigDecimal finalCash) {
        this.finalCash = finalCash;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(LocalDateTime closureDate) {
        this.closureDate = closureDate;
    }

    @Override
    public String toString() {
        return "CashierClosureSearchDto{" +
                "openingDate=" + openingDate +
                ", initialCash=" + initialCash +
                ", salesCard=" + salesCard +
                ", salesCash=" + salesCash +
                ", usedVouchers=" + usedVouchers +
                ", deposit=" + deposit +
                ", withdrawal=" + withdrawal +
                ", lostCard=" + lostCard +
                ", lostCash=" + lostCash +
                ", finalCash=" + finalCash +
                ", comment='" + comment + '\'' +
                ", closureDate=" + closureDate +
                '}';
    }
}
