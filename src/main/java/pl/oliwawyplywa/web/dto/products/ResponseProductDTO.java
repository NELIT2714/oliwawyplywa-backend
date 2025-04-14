package pl.oliwawyplywa.web.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseProductDTO extends ProductDTO {

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("product_options")
    private List<ResponseProductOptionDTO> productOptions;

    public ResponseProductDTO() {
        super();
    }

    public ResponseProductDTO(int productId, int categoryId, String productName, String productDescription, List<ResponseProductOptionDTO> productOptions) {
        super(categoryId, productName, productDescription);
        this.productId = productId;
        this.productOptions = productOptions;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public List<ResponseProductOptionDTO> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ResponseProductOptionDTO> productOptions) {
        this.productOptions = productOptions;
    }
}
