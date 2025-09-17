package pl.oliwawyplywa.web.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.Order;
import reactor.core.publisher.Mono;

@Repository
public interface OrdersRepository extends ReactiveCrudRepository<Order, Integer> {
    Mono<Order> getOrderByOrderId(int orderId);
}
