package pl.oliwawyplywa.web.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductItem {

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("quantity")
    private int quantity;

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
