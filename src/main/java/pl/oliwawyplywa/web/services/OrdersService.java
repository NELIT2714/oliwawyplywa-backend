package pl.oliwawyplywa.web.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.oliwawyplywa.web.dto.orders.CreateOrder;
import pl.oliwawyplywa.web.dto.orders.OrderItemDTO;
import pl.oliwawyplywa.web.dto.orders.OrderResponse;
import pl.oliwawyplywa.web.dto.payments.TpayTransactionCreatedResponse;
import pl.oliwawyplywa.web.enums.OrderStatuses;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.OrderItemsRepository;
import pl.oliwawyplywa.web.repositories.OrdersRepository;
import pl.oliwawyplywa.web.repositories.ProductOptionsRepository;
import pl.oliwawyplywa.web.schemas.Order;
import pl.oliwawyplywa.web.schemas.OrderItem;
import pl.oliwawyplywa.web.services.tpay.TpayPaymentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductOptionsRepository productOptionRepository;
    private final TpayPaymentService tpayPaymentService;

    public OrdersService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, ProductOptionsRepository productOptionRepository, TpayPaymentService tpayPaymentService) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.productOptionRepository = productOptionRepository;
        this.tpayPaymentService = tpayPaymentService;
    }

    public Mono<OrderResponse> getOrder(int orderId) {
        return ordersRepository.getOrderByOrderId(orderId)
            .flatMap(order -> {
                OrderResponse orderResponse = new OrderResponse(order);

                return orderItemsRepository.getOrderItemsByOrderId(orderId)
                    .map(OrderItemDTO::new)
                    .collectList()
                    .map(items -> {
                        orderResponse.setItems(items);
                        return orderResponse;
                    });
            });
    }

    @Transactional
    public Mono<String> createOrder(CreateOrder createOrderDTO) {
        Order order = createOrderFromDTO(createOrderDTO);
        return saveOrderAndProcessItems(order, createOrderDTO.getProducts())
            .flatMap(this::createTransactionAndGetPaymentUrl);
    }

    private Order createOrderFromDTO(CreateOrder createOrderDTO) {
        return new Order(
            createOrderDTO.getEmail(),
            createOrderDTO.getFullName(),
            createOrderDTO.getAddress(),
            OrderStatuses.CREATED
        );
    }

    private Mono<Order> saveOrderAndProcessItems(Order order, List<OrderItemDTO> products) {
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
                    .switchIfEmpty(Mono.error(new HTTPException(HttpStatus.NOT_FOUND,
                        "Product option " + product.getProductOptionId() + " not found")))
            );
    }

    private Mono<String> createTransactionAndGetPaymentUrl(Order order) {
        return tpayPaymentService.createTransaction(order)
            .map(TpayTransactionCreatedResponse::getTransactionPaymentUrl);
    }
}
