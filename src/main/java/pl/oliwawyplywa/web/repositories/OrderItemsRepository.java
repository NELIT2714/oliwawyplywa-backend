package pl.oliwawyplywa.web.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.OrderItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
public interface OrderItemsRepository extends ReactiveCrudRepository<OrderItem, Integer> {
    @Query("SELECT COALESCE(SUM(price * quantity), 0) FROM tbl_order_items WHERE id_order = :orderId")
    Mono<BigDecimal> calculateTotalByOrderId(@Param("orderId") int orderId);

    Flux<OrderItem> getOrderItemsByOrderId(int orderId);

    @Query("""
        SELECT oi.id_item, oi.id_order, oi.id_product_option, oi.quantity, oi.price,
               p.product_name, po.option_name
        FROM tbl_order_items oi
        JOIN tbl_product_options po ON oi.id_product_option = po.id_product_option
        JOIN tbl_products p ON po.id_product = p.id_product
        WHERE oi.id_order = :orderId
    """)
    Flux<OrderItem> getOrderItemsWithOptionsByOrderId(int orderId);
}
