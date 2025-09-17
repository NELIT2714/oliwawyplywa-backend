package pl.oliwawyplywa.web.services.tpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.oliwawyplywa.web.dto.payments.TpayTransactionCreatedResponse;
import pl.oliwawyplywa.web.schemas.Order;
import pl.oliwawyplywa.web.services.OrderCalculationService;
import reactor.core.publisher.Mono;

import java.math.RoundingMode;
import java.util.Map;

@Service
public class TpayPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(TpayPaymentService.class);

    private final OrderCalculationService orderCalculationService;
    private final TokenManager tokenManager;

    private final TpayConfig config;

    public TpayPaymentService(OrderCalculationService orderCalculationService, TokenManager tokenManager, TpayConfig config) {
        this.orderCalculationService = orderCalculationService;
        this.tokenManager = tokenManager;
        this.config = config;
    }

    public Mono<TpayTransactionCreatedResponse> createTransaction(Order order) {
        return orderCalculationService.calculateOrderTotal(order.getOrderId())
            .flatMap(totalAmount -> {
                Map<String, Object> requestBody = Map.of(
                    "amount", totalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue(),
                    "description", "Transakcja na zamówienie #%d".formatted(order.getOrderId()),
                    "payer", Map.of(
                        "email", order.getEmail(),
                        "name", order.getFullName()
                    ),
                    "currency", "PLN"
                );

                return tokenManager.getToken()
                    .flatMap(token ->
                        WebClient.create(config.getApiUrl())
                            .post()
                            .uri("/transactions")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> Mono.error(new RuntimeException("Tpay API error: " + errorBody + ", status: " + clientResponse.statusCode()))))
                            .bodyToMono(TpayTransactionCreatedResponse.class)
                            .doOnSuccess(response -> logger.info("Tpay: Transaction created: ID={}", response.getTransactionId()))
                            .doOnError(error -> logger.error("Error creating transaction for order {}: {}", order.getOrderId(), error.getMessage()))
                    );
            });
    }
}
