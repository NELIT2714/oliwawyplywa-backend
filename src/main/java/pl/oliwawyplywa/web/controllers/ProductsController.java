package pl.oliwawyplywa.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.products.CreateProductDTO;
import pl.oliwawyplywa.web.dto.products.ResponseProductDTO;
import pl.oliwawyplywa.web.schemas.Category;
import pl.oliwawyplywa.web.schemas.Product;
import pl.oliwawyplywa.web.services.ImageStorageService;
import pl.oliwawyplywa.web.services.ProductsService;
import pl.oliwawyplywa.web.utils.mappers.ProductMapper;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/products")
@Tag(name = "Products")
public class ProductsController {

    private final ProductsService productsService;
    private final ProductMapper productMapper;
    private final ImageStorageService imageStorageService;

    public ProductsController(ProductsService productsService, ProductMapper productMapper, ImageStorageService imageStorageService) {
        this.productsService = productsService;
        this.productMapper = productMapper;
        this.imageStorageService = imageStorageService;
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
    public Mono<ResponseEntity<Map<String, Object>>> getProducts() {
        return productsService.getProducts()
            .collectList()
            .map(products -> ResponseEntity.ok(Map.of(
                "status", true,
                "products", products
            )));
    }

    @Operation(
        summary = "Create product",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(implementation = CreateProductDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Create example product",
                        value =
                            """
                                {
                                  "category_id": 1,
                                  "product_name": "Test product",
                                  "product_description": "Test description",
                                  "product_options": [
                                    {
                                      "option_label": "Option name 1",
                                      "option_price": 10.00
                                    },
                                    {
                                      "option_label": "Option name 2",
                                      "option_price": 20.00
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
                          "product_id": 1,
                          "category_id": 1,
                          "product_name": "Test product",
                          "product_description": "Test description",
                          "product_options": [
                            {
                              "option_id": 1,
                              "option_label": "Option name 1",
                              "option_price": 10.00
                            },
                            {
                              "option_id": 2,
                              "option_label": "Option name 2",
                              "option_price": 20.00
                            }
                          ]
                        }
                        """
            )
        )
    )
    @PostMapping(consumes = "multipart/form-data")
    public Mono<ResponseEntity<Map<String, Object>>> createProduct(
        @RequestPart(name = "data") @Schema(implementation = CreateProductDTO.class) CreateProductDTO productDTO,
        @RequestPart(name = "product_image") @Schema(type = "string", format = "binary") FilePart productImage
    ) {
        return imageStorageService.saveImage(productImage)
            .flatMap(image -> productsService.createProduct(productDTO, image))
            .map(product -> ResponseEntity.ok(Map.of("status", true, "product", product)));
    }
}
