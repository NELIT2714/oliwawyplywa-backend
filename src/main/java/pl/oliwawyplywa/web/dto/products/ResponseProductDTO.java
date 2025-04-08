package pl.oliwawyplywa.web.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseProductDTO extends ProductDTO {

    @JsonProperty("product_id")
    private int productId;

    public ResponseProductDTO() {
        super();
    }

    public ResponseProductDTO(int categoryId, String productName, String productDescription, List<ProductOptionDTO> productOptions, int productId) {
        super(categoryId, productName, productDescription, productOptions);
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
