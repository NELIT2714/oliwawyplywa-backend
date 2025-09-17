package pl.oliwawyplywa.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.orders.CreateOrder;
import pl.oliwawyplywa.web.schemas.Category;
import pl.oliwawyplywa.web.services.OrdersService;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/orders")
@Validated
@Tag(name = "Orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Operation(summary = "Get order by ID")
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                        {
                          "status": true,
                          "order": {
                            "order_id": 1,
                            "email": "example@email.com",
                            "address": "Warszawa ...",
                            "status": "PAID",
                            "created_at": "2025-12-31 23:59:59",
                            "items": [
                              {
                                "product_option_id": 1,
                                "quantity": 1
                              },
                              {
                                "product_option_id": 2,
                                "quantity": 5
                              }
                            ]
                          }
                        }
                        """
            )
        )
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getOrder(@PathVariable("id") int orderId) {
        return ordersService.getOrder(orderId).map(order -> ResponseEntity.ok(Map.of("status", true, "order", order)));
    }

    @Operation(
        summary = "Create order",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Category.class),
                examples = {
                    @ExampleObject(
                        name = "Create example order",
                        value =
                            """
                                {
                                  "full_name": "Test Test",
                                  "email": "example@email.com",
                                  "address": "Warszawa ...",
                                  "products": [
                                    {
                                      "product_option_id": 1,
                                      "quantity": 1
                                    },
                                    {
                                      "product_option_id": 2,
                                      "quantity": 5
                                    }
                                  ]
                                }
                                """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                        {
                          "status": true,
                          "payment_url": "https://secure.sandbox.tpay.com/?title=TR-123&uid=123"
                        }
                        """
            )
        )
    )
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createOrder(@RequestBody CreateOrder createOrderDTO) {
        return ordersService.createOrder(createOrderDTO).map(paymentUrl -> ResponseEntity.ok(Map.of("status", true, "payment_url", paymentUrl)));
    }
}
