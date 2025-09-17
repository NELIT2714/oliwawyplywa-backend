package pl.oliwawyplywa.web.schemas;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tbl_products")
public class Product {

    @Id
    @Column("id_product")
    private int idProduct;

    @Column("id_category")
    private int categoryId;

    @NotBlank
    private String productName;

    private String productDescription;

    public Product() {
    }

    public Product(int categoryId, String productName, String productDescription) {
        this.categoryId = categoryId;
        this.productName = productName;
        this.productDescription = productDescription;
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
}
