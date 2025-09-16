package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("tbl_products_options")
public class ProductOption {

    @Id
    @Column("id_product_option")
    private int idProductOption;

    @Column("id_product")
    @JsonIgnore
    private int productId;

    @NotBlank
    @Column("option_label")
    private String optionLabel;

    @Column("option_price")
    private BigDecimal optionPrice;

    public ProductOption() {
    }

    public ProductOption(int productId, String optionLabel, BigDecimal optionPrice) {
        this.productId = productId;
        this.optionLabel = optionLabel;
        this.optionPrice = optionPrice;
    }

    public int getIdProductOption() {
        return idProductOption;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public BigDecimal getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(BigDecimal optionPrice) {
        this.optionPrice = optionPrice;
    }
}
