package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tbl_products_options")
public class ProductOption {

    @Id
    @Column("id_product_option")
    private Integer idProductOption;

    @Column("id_product")
    @JsonIgnore
    private Integer productId;

    @NotBlank
    @Column("option_label")
    private String optionLabel;

    @Column("option_price")
    private Float optionPrice;

    public ProductOption() {
    }

    public ProductOption(Integer productId, String optionLabel, Float optionPrice) {
        this.productId = productId;
        this.optionLabel = optionLabel;
        this.optionPrice = optionPrice;
    }

    public Integer getIdProductOption() {
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

    public Float getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(Float optionPrice) {
        this.optionPrice = optionPrice;
    }
}
