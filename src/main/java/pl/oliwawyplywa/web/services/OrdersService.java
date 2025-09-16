package pl.oliwawyplywa.web.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.oliwawyplywa.web.dto.orders.CreateOrder;
import pl.oliwawyplywa.web.dto.orders.ProductItem;
import pl.oliwawyplywa.web.enums.OrderStatuses;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.OrderItemsRepository;
import pl.oliwawyplywa.web.repositories.OrdersRepository;
import pl.oliwawyplywa.web.repositories.ProductOptionsRepository;
import pl.oliwawyplywa.web.schemas.Order;
import pl.oliwawyplywa.web.schemas.OrderItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Transactional
    public Mono<Order> createOrder(CreateOrder createOrderDTO) {
        String email = createOrderDTO.getEmail();
        String address = createOrderDTO.getAddress();
        List<ProductItem> products = createOrderDTO.getProducts();

        Order order = new Order(email, address, OrderStatuses.CREATED);

        return ordersRepository.save(order)
            .flatMap(savedOrder -> Flux.fromIterable(products)
                .flatMap(p ->
                    productOptionRepository.findById(p.getProductId())
                        .map(option -> new OrderItem(
                            savedOrder.getOrderId(),
                            option.getIdProductOption(),
                            p.getQuantity(),
                            option.getOptionPrice()
                        )).switchIfEmpty(Mono.error(new HTTPException(HttpStatus.NOT_FOUND, "Product not found")))
                )
                .collectList()
                .flatMap(items -> orderItemsRepository.saveAll(items).collectList())
                .then(Mono.just(savedOrder)));
    }
}
