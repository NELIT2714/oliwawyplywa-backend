package pl.oliwawyplywa.web.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.dto.orders.CreateOrder;
import pl.oliwawyplywa.web.dto.orders.OrderItemDTO;
import pl.oliwawyplywa.web.dto.orders.OrderResponse;
import pl.oliwawyplywa.web.enums.OrderStatuses;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.OrderItemsRepository;
import pl.oliwawyplywa.web.repositories.OrdersRepository;
import pl.oliwawyplywa.web.repositories.ProductOptionsRepository;
import pl.oliwawyplywa.web.schemas.Order;
import pl.oliwawyplywa.web.schemas.OrderItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductOptionsRepository productOptionRepository;

    public OrdersService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, ProductOptionsRepository productOptionRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.productOptionRepository = productOptionRepository;
    }

    public Mono<Order> getOrder(int orderId) {
        return ordersRepository.findById(orderId);
    }

    public Mono<OrderResponse> getOrderResponse(int orderId) {
        return ordersRepository.getOrderByOrderId(orderId)
            .flatMap(order -> {
                OrderResponse orderResponse = new OrderResponse(order);

                Mono<List<OrderItemDTO>> itemsMono = orderItemsRepository.getOrderItemsByOrderId(orderId)
                    .map(OrderItemDTO::new)
                    .collectList();

                Mono<BigDecimal> totalMono = orderItemsRepository.calculateTotalByOrderId(orderId).defaultIfEmpty(BigDecimal.ZERO);

                return Mono.zip(itemsMono, totalMono)
                    .map(tuple -> {
                        List<OrderItemDTO> items = tuple.getT1();
                        BigDecimal total = tuple.getT2();

                        orderResponse.setItems(items);
                        orderResponse.setPrice(total);

                        return orderResponse;
                    });
            });
    }

    public Order createOrderFromDTO(CreateOrder createOrderDTO) {
        return new Order(
            createOrderDTO.getEmail(),
            createOrderDTO.getFullName(),
            createOrderDTO.getAddress(),
            OrderStatuses.CREATED
        );
    }

    public Mono<Order> saveOrderAndProcessItems(Order order, List<OrderItemDTO> products) {
        return ordersRepository.save(order)
            .flatMap(savedOrder ->
                createOrderItems(savedOrder, products)
                    .collectList()
                    .flatMap(items -> orderItemsRepository.saveAll(items)
                        .collectList()
                        .thenReturn(savedOrder)
                    )
            );
    }

    private Flux<OrderItem> createOrderItems(Order order, List<OrderItemDTO> products) {
        return Flux.fromIterable(products)
            .flatMap(product ->
                productOptionRepository.findById(product.getProductOptionId())
                    .map(option -> new OrderItem(
                        order.getOrderId(),
                        option.getIdProductOption(),
                        product.getQuantity(),
                        option.getOptionPrice()
                    ))
                    .switchIfEmpty(Mono.error(new HTTPException(HttpStatus.NOT_FOUND, "Product option " + product.getProductOptionId() + " not found")))
            );
    }
}
