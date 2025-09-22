package pl.oliwawyplywa.web.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pl.oliwawyplywa.web.enums.OrderStatuses;

import java.time.Instant;
import java.time.LocalDateTime;

@Table("tbl_orders")
public class Order {

    @Id
    @Column("id_order")
    @JsonProperty("order_id")
    private int orderId;

    @Column("email")
    private String email;

    @Column("full_name")
    private String fullName;

    @Column("address")
    private String address;

    @Column("status")
    private OrderStatuses status;

    @Column("created_at")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public Order(String email, String fullName, String address, OrderStatuses status) {
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OrderStatuses getStatus() {
        return status;
    }

    public void setStatus(OrderStatuses status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
