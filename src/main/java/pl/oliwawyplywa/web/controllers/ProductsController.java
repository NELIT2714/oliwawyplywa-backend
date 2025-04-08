package pl.oliwawyplywa.web.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oliwawyplywa.web.dto.products.ProductDTO;
import pl.oliwawyplywa.web.schemas.Product;
import pl.oliwawyplywa.web.services.ProductsService;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductDTO productDTO) {
        return productsService.createProduct(productDTO);
    }

}
