package pl.oliwawyplywa.web.services.tpay;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import pl.oliwawyplywa.web.config.TpayConfig;
import pl.oliwawyplywa.web.dto.payments.TpayTransactionCreatedResponse;
import pl.oliwawyplywa.web.enums.OrderStatuses;
import pl.oliwawyplywa.web.repositories.OrdersRepository;
import pl.oliwawyplywa.web.schemas.Order;
import pl.oliwawyplywa.web.services.OrderCalculationService;
import pl.oliwawyplywa.web.services.OrdersService;
import reactor.core.publisher.Mono;

import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class TpayPaymentService {

    @Value("${payments.tpay.secret_sum_code}")
    private String secretSumCode;

    private static final Logger logger = LoggerFactory.getLogger(TpayPaymentService.class);

    private final OrderCalculationService orderCalculationService;
    private final TpaySignatureService tpaySignatureService;
    private final OrdersService ordersService;
    private final OrdersRepository ordersRepository;
    private final TokenManager tokenManager;

    private final TpayConfig config;

    public TpayPaymentService(OrderCalculationService orderCalculationService, TpaySignatureService tpaySignatureService, OrdersService ordersService, OrdersRepository ordersRepository, TokenManager tokenManager, TpayConfig config) {
        this.orderCalculationService = orderCalculationService;
        this.tpaySignatureService = tpaySignatureService;
        this.ordersService = ordersService;
        this.ordersRepository = ordersRepository;
        this.tokenManager = tokenManager;
        this.config = config;
    }

    public Mono<TpayTransactionCreatedResponse> createTransaction(Order order) {
        return orderCalculationService.calculateOrderTotal(order.getOrderId())
            .flatMap(totalAmount -> {
                Map<String, Object> requestBody = Map.of(
                    "amount", totalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue(),
                    "description", "Transakcja na zamówienie #%d".formatted(order.getOrderId()),
                    "hiddenDescription", String.valueOf(order.getOrderId()),
                    "payer", Map.of(
                        "email", order.getEmail(),
                        "name", order.getFullName(),
                        "address", order.getAddress()
                    ),
                    "callbacks", Map.of(
                        "payerUrls", Map.of(
                            "success", "https://oliwawyplywa.pl/",
                            "error", "https://oliwawyplywa.pl/"
                        )
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

    public Mono<String> handleCallback(String jws, ServerHttpRequest request) {
        return request.getBody()
            .reduce("", (accumulated, dataBuffer) -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                return accumulated + new String(bytes, StandardCharsets.UTF_8);
            })
            .flatMap(body -> {
                logger.debug("Received raw body: {}", body);
                return tpaySignatureService.verifySignature(jws, body)
                    .flatMap(isValid -> {
                        if (!isValid) {
                            logger.warn("JWS signature verification failed");
                            return Mono.just("FALSE - Invalid JWS signature");
                        }

                        MultiValueMap<String, String> params = UriComponentsBuilder.newInstance()
                            .query(body)
                            .build()
                            .getQueryParams();

                        return verifyMd5sum(params)
                            .flatMap(isValidMd5 -> {
                                if (!isValidMd5) {
                                    return Mono.just("FALSE - Invalid md5sum");
                                }

                                String trCrc = params.getFirst("tr_crc");
                                if (trCrc == null) {
                                    logger.error("Missing tr_crc in transaction parameters");
                                    return Mono.just("FALSE - Missing tr_crc");
                                }

                                int orderId;
                                try {
                                    orderId = Integer.parseInt(trCrc);
                                } catch (NumberFormatException e) {
                                    logger.error("Invalid tr_crc format: {}", trCrc, e);
                                    return Mono.just("FALSE - Invalid tr_crc format");
                                }

                                return updateOrderStatus(orderId).thenReturn("TRUE")
                                    .onErrorResume(e -> {
                                        logger.error("Error processing transaction", e);
                                        return Mono.just("FALSE - Processing error");
                                    });
                            });
                    });
            })
            .defaultIfEmpty("FALSE - Empty body");
    }

    private Mono<Order> updateOrderStatus(int orderId) {
        return ordersService.getOrder(orderId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found for ID: " + orderId)))
            .flatMap(order -> {
                if (order.getStatus() == OrderStatuses.PAID) {
                    logger.info("Order {} already processed, skipping", orderId);
                    return Mono.empty();
                }
                logger.info("Updating order {} status to {}", orderId, OrderStatuses.PAID);
                order.setStatus(OrderStatuses.PAID);
                return ordersService.getOrder(orderId).flatMap(ordersRepository::save);
            });
    }

    private Mono<Boolean> verifyMd5sum(MultiValueMap<String, String> params) {
        String id = params.getFirst("id");
        String trId = params.getFirst("tr_id");
        String trAmount = params.getFirst("tr_amount");
        String trCrc = params.getFirst("tr_crc");
        String md5sumReceived = params.getFirst("md5sum");

        if (id == null || trId == null || trAmount == null || trCrc == null || md5sumReceived == null) {
            logger.warn("Missing required parameters for md5sum verification");
            return Mono.just(false);
        }

        String computedMd5 = DigestUtils.md5Hex(id + trId + trAmount + trCrc + secretSumCode);
        if (!computedMd5.equals(md5sumReceived)) {
            logger.warn("md5sum verification failed: computed={}, received={}", computedMd5, md5sumReceived);
            return Mono.just(false);
        }

        logger.debug("md5sum verification succeeded");
        return Mono.just(true);
    }
}
