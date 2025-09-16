package pl.oliwawyplywa.web.schemas;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("tbl_order_items")
public class OrderItem {

    @Id
    @Column("id_item")
    private int itemId;

    @Column("id_order")
    private int orderId;

    @Column("id_product_option")
    private int productOptionId;

    @Column("quantity")
    private int quantity;

    @Column("price")
    private BigDecimal price;

    public OrderItem(int orderId, int productOptionId, int quantity, BigDecimal price) {
        this.orderId = orderId;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductOptionId() {
        return productOptionId;
    }

    public void setProductOptionId(int productOptionId) {
        this.productOptionId = productOptionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
