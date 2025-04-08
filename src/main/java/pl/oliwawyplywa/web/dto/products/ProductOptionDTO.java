package pl.oliwawyplywa.web.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductOptionDTO {

    @JsonProperty("option_label")
    private String optionLabel;

    @JsonProperty("option_price")
    private float optionPrice;

    public ProductOptionDTO() {
    }

    public ProductOptionDTO(String optionLabel, float optionPrice) {
        this.optionLabel = optionLabel;
        this.optionPrice = optionPrice;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public float getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(float optionPrice) {
        this.optionPrice = optionPrice;
    }
}
