package pl.oliwawyplywa.web.schemas;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Table("tbl_products")
public class Product {

    @Id
    @Column("id_product")
    private Integer idProduct;

    @Column("id_category")
    private Integer categoryId; // Ссылка на Category через id

    @NotBlank
    private String productName;

    private String productDescription;

    public Product() {}

    public Product(Integer categoryId, String productName, String productDescription) {
        this.categoryId = categoryId;
        this.productName = productName;
        this.productDescription = productDescription;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
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
