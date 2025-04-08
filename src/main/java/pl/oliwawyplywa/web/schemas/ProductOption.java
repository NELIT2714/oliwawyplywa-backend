package pl.oliwawyplywa.web.schemas;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_products_options")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product_option")
    private int idProductOption;

    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id_product")
    private Product product;

    @Column(name = "option_label")
    private String optionLabel;

    @Column(name = "option_price")
    private float optionPrice;

    public ProductOption(Product product, String optionLabel, float optionPrice) {
        this.product = product;
        this.optionLabel = optionLabel;
        this.optionPrice = optionPrice;
    }

    public ProductOption() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public float getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(float optionPrice) {
        this.optionPrice = optionPrice;
    }
}
