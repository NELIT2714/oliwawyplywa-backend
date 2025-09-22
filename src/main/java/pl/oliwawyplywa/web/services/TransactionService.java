package pl.oliwawyplywa.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import pl.oliwawyplywa.web.enums.OrderStatuses;
import pl.oliwawyplywa.web.repositories.OrdersRepository;
import pl.oliwawyplywa.web.schemas.Order;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final OrdersService ordersService;
    private final OrdersRepository ordersRepository;

    public TransactionService(OrdersService ordersService, OrdersRepository ordersRepository) {
        this.ordersService = ordersService;
        this.ordersRepository = ordersRepository;
    }

    public Mono<Order> processTransaction(MultiValueMap<String, String> params) {
        String trId = params.getFirst("tr_id");
        String trCrc = params.getFirst("tr_crc");
        String trAmount = params.getFirst("tr_amount");

        if (trCrc == null) {
            logger.error("Missing tr_crc in transaction parameters");
            return Mono.error(new IllegalArgumentException("Missing tr_crc"));
        }

        int orderId;
        try {
            orderId = Integer.parseInt(trCrc);
        } catch (NumberFormatException e) {
            logger.error("Invalid tr_crc format: {}", trCrc, e);
            return Mono.error(new IllegalArgumentException("Invalid tr_crc format: " + trCrc));
        }

        return ordersService.getOrder(orderId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found for ID: " + orderId)))
            .flatMap(order -> {
                if (order.getStatus() == OrderStatuses.PAID) {
                    logger.info("Order {} already processed, skipping", orderId);
                    return Mono.empty();
                }

                logger.info("Processing transaction: tr_id={}, tr_crc={}, amount={}", trId, trCrc, trAmount);

                logger.info("Send email logic here...");

                order.setStatus(OrderStatuses.PAID);
                return ordersRepository.save(order);
            });
    }

}
