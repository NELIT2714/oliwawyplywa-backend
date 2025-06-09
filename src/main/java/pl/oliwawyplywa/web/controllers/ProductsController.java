package pl.oliwawyplywa.web.controllers;

import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.products.CreateProductDTO;
import pl.oliwawyplywa.web.dto.products.ResponseProductDTO;
import pl.oliwawyplywa.web.schemas.Product;
import pl.oliwawyplywa.web.services.ProductsService;
import pl.oliwawyplywa.web.utils.mappers.ProductMapper;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductsController {

    private final ProductsService productsService;
    private final ProductMapper productMapper;

    public ProductsController(ProductsService productsService, ProductMapper productMapper) {
        this.productsService = productsService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ResponseProductDTO> getProducts() {
        List<Product> products = productsService.getProducts();
        return products.stream()
            .map(productMapper::mapProductToDTO)
            .toList();
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductDTO productDTO) {
        return productsService.createProduct(productDTO);
    }

}
