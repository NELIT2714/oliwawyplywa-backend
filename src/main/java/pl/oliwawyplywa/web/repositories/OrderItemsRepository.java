package pl.oliwawyplywa.web.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.OrderItem;

@Repository
public interface OrderItemsRepository extends ReactiveCrudRepository<OrderItem, Integer> {
}
