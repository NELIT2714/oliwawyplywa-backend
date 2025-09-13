package pl.oliwawyplywa.web.services;

import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.dto.products.CreateProductDTO;
import pl.oliwawyplywa.web.repositories.ProductOptionsRepository;
import pl.oliwawyplywa.web.repositories.ProductsRepository;
import pl.oliwawyplywa.web.schemas.Product;
import pl.oliwawyplywa.web.schemas.ProductOption;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductOptionsRepository productOptionsRepository;
    private final CategoriesService categoriesService;

    public ProductsService(ProductsRepository productsRepository,
                           ProductOptionsRepository productOptionsRepository,
                           CategoriesService categoriesService) {
        this.productsRepository = productsRepository;
        this.productOptionsRepository = productOptionsRepository;
        this.categoriesService = categoriesService;
    }

    public Flux<Product> getProducts() {
        return productsRepository.findAll();
    }

    public Mono<Product> createProduct(CreateProductDTO dto) {
        return categoriesService.getCategory(dto.getCategoryId()).flatMap(category -> {
            Product product = new Product(category.getIdCategory(),
                dto.getProductName(),
                dto.getProductDescription());

            return productsRepository.save(product).flatMap(savedProduct -> {
                List<ProductOption> options = dto.getProductOptions().stream()
                    .map(o -> new ProductOption(savedProduct.getIdProduct(), o.getOptionLabel(), o.getOptionPrice()))
                    .toList();

                return Flux.fromIterable(options)
                    .flatMap(productOptionsRepository::save)
                    .then(Mono.just(savedProduct));
            });
        });
    }
}
