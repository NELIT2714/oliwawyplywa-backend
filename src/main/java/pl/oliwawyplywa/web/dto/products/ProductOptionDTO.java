package pl.oliwawyplywa.web.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProductOptionDTO {

    @JsonProperty("option_label")
    private String optionLabel;

    @JsonProperty("option_price")
    private BigDecimal optionPrice;

    public ProductOptionDTO() {
    }

    public ProductOptionDTO(String optionLabel, BigDecimal optionPrice) {
        this.optionLabel = optionLabel;
        this.optionPrice = optionPrice;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public BigDecimal getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(BigDecimal optionPrice) {
        this.optionPrice = optionPrice;
    }
}
