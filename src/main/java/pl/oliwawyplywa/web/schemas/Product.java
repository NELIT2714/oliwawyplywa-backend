package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tbl_products")
public class Product {

    @Id
    @Column("id_product")
    @JsonProperty("product_id")
    private int idProduct;

    @Column("id_category")
    @JsonProperty("category_id")
    private int categoryId;

    @Column("product_image")
    @JsonProperty("product_image")
    private String productImage;

    @Column("product_name")
    @JsonProperty("product_name")
    private String productName;

    @Column("product_description")
    @JsonProperty("product_description")
    private String productDescription;

    public Product() {
    }

    public Product(int categoryId, String productName, String productDescription, String productImage) {
        this.categoryId = categoryId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
    }

    public int getIdProduct() {
        return idProduct;
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

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
