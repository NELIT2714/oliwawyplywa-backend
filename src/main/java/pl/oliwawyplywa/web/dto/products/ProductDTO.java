package pl.oliwawyplywa.web.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductDTO {

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_description")
    private String productDescription;

    @JsonProperty("product_options")
    private List<ProductOptionDTO> productOptions;

    public ProductDTO(int categoryId, String productName, String productDescription, List<ProductOptionDTO> productOptions) {
        this.categoryId = categoryId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productOptions = productOptions;
    }

    public ProductDTO() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public List<ProductOptionDTO> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductOptionDTO> productOptions) {
        this.productOptions = productOptions;
    }
}
