package pl.oliwawyplywa.web.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateProductDTO extends ProductDTO {

    @JsonProperty("product_options")
    private List<ProductOptionDTO> productOptions;

    public CreateProductDTO(int categoryId, String productName, String productDescription, List<ProductOptionDTO> productOptions) {
        super(categoryId, productName, productDescription, null);
        this.productOptions = productOptions;
    }

    public List<ProductOptionDTO> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductOptionDTO> productOptions) {
        this.productOptions = productOptions;
    }
}
