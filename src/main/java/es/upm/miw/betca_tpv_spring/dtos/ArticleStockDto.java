package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ArticleStockDto extends ArticleMinimumDto {

    private Integer stock;

    private Integer soldUnits;

    public ArticleStockDto() {
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSoldUnits() {
        return soldUnits;
    }

    public void setSoldUnits(Integer soldUnits) {
        this.soldUnits = soldUnits;
    }

    @Override
    public String toString() {
        return "ArticleStockDto{" +
                "code=" + getCode() +
                ", description=" + getDescription() +
                ", stock=" + stock +
                ", soldUnits=" + soldUnits +
                '}';
    }
}
