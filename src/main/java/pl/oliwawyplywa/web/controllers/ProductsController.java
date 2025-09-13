package pl.oliwawyplywa.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.products.CreateProductDTO;
import pl.oliwawyplywa.web.dto.products.ResponseProductDTO;
import pl.oliwawyplywa.web.services.ProductsService;
import pl.oliwawyplywa.web.utils.mappers.ProductMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/products")
@Tag(name = "Products")
public class ProductsController {

    private final ProductsService productsService;
    private final ProductMapper productMapper;

    public ProductsController(ProductsService productsService, ProductMapper productMapper) {
        this.productsService = productsService;
        this.productMapper = productMapper;
    }

    @GetMapping
    @Operation(summary = "Get all products")
    @ApiResponse(
        responseCode = "200",
        description = "Products list",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                        [
                          {
                            "category_id": 1,
                            "product_name": "Products name",
                            "product_description": "Product description",
                            "product_id": 1,
                            "product_options": [
                              {
                                "option_label": "Option name",
                                "option_price": 100,
                                "option_id": 1
                              },
                              {
                                "option_label": "Option name",
                                "option_price": 100,
                                "option_id": 2
                              }
                            ]
                          }
                        ]
                        """
            )
        )
    )
    public Flux<ResponseProductDTO> getProducts() {
        return productsService.getProducts()
            .flatMap(productMapper::mapProductToDTO);
    }

    @PostMapping
    public Mono<ResponseProductDTO> createProduct(@RequestBody CreateProductDTO productDTO) {
        return productsService.createProduct(productDTO)
            .flatMap(productMapper::mapProductToDTO);
    }
}
