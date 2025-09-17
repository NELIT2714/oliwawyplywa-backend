package pl.oliwawyplywa.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.OrderItemsRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class OrderCalculationService {

    private static final Logger log = LoggerFactory.getLogger(OrderCalculationService.class);

    private final OrderItemsRepository orderItemsRepository;

    public OrderCalculationService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    public Mono<BigDecimal> calculateOrderTotal(int orderId) {
        return orderItemsRepository.calculateTotalByOrderId(orderId)
            .switchIfEmpty(Mono.error(new HTTPException(HttpStatus.NOT_FOUND, "Order %d has no products".formatted(orderId))))
            .flatMap(total -> {
                if (total.compareTo(BigDecimal.ZERO) <= 0) {
                    return Mono.error(new IllegalArgumentException("Total amount must be positive for order %d".formatted(orderId)));
                }
                return Mono.just(total);
            });
    }
}