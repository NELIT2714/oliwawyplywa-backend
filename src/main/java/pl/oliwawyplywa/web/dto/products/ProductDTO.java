package pl.oliwawyplywa.web.dto.products;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDTO {

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_description")
    private String productDescription;

    @JsonProperty("product_image")
    private String productImage;

    public ProductDTO(int categoryId, String productName, String productDescription, String productImage) {
        this.categoryId = categoryId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
