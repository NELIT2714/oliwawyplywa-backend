package pl.oliwawyplywa.web.schemas;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import pl.oliwawyplywa.web.enums.OrderStatuses;

@Table("tbl_orders")
public class Order {

    @Id
    @Column("id_order")
    private int orderId;

    @Column("email")
    private String email;

    @Column("address")
    private String address;

    @Column("status")
    private OrderStatuses status;

    public Order(String email, String address, OrderStatuses status) {
        this.email = email;
        this.address = address;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
