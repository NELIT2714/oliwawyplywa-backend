package pl.oliwawyplywa.web.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.oliwawyplywa.web.enums.OrderStatuses;
import pl.oliwawyplywa.web.schemas.Order;

import java.time.Instant;
import java.util.List;

public class OrderResponse {

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("status")
    private OrderStatuses status;

    @JsonProperty("items")
    private List<OrderItemDTO> items;

    @JsonProperty("created_at")
    private Instant createdAt;

    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.email = order.getEmail();
        this.address = order.getAddress();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
    }

    public int getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public OrderStatuses getStatus() {
        return status;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
