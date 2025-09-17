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
}
