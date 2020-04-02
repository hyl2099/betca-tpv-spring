package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.Quarter;

import java.util.ArrayList;
import java.util.List;

public class QuarterVATDto {

    private Quarter quarter;
    private List<TaxDto> taxes;

    public QuarterVATDto(Quarter quarter) {
        this.quarter = quarter;
        taxes = new ArrayList<>();
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public List<TaxDto> getTaxes() {
        return taxes;
    }

    @Override
    public String toString() {
        return "QuarterVATDto{" +
                "quarter='" + quarter + '\'' +
                ", taxes=" + taxes +
                '}';
    }
}
