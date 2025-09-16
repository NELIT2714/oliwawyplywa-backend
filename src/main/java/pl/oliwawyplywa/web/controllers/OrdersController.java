package pl.oliwawyplywa.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                              "email": "example@email.com",
                              "address": "Warszawa ...",
                              "products": [
                                {
                                  "product_id": 1,
                                  "quantity": 1
                                },
                                {
                                  "product_id": 2,
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
                      "category": {
                        "category_id": 1,
                        "category_name": "Category name"
                      },
                      "status": true
                    }
                    """
            )
        )
    )
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createOrder(@RequestBody CreateOrder createOrderDTO) {
        return ordersService.createOrder(createOrderDTO).map(order -> ResponseEntity.ok(Map.of("status", true, "order", order)));
    }
}
