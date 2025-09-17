package pl.oliwawyplywa.web.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.oliwawyplywa.web.schemas.OrderItem;

public class OrderItemDTO {

    @JsonProperty("product_option_id")
    private int productOptionId;

    @JsonProperty("quantity")
    private int quantity;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItem orderItem) {
        this.productOptionId = orderItem.getProductOptionId();
        this.quantity = orderItem.getQuantity();
    }

    public int getProductOptionId() {
        return productOptionId;
    }

    public int getQuantity() {
        return quantity;
    }
}
