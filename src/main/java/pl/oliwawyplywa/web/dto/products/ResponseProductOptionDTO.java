package pl.oliwawyplywa.web.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseProductOptionDTO extends ProductOptionDTO {

    @JsonProperty("option_id")
    private int optionId;

    public ResponseProductOptionDTO() {
        super();
    }

    public ResponseProductOptionDTO(String optionLabel, float optionPrice, int optionId) {
        super(optionLabel, optionPrice);
        this.optionId = optionId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}
