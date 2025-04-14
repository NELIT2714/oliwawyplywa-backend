package pl.oliwawyplywa.web.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.dto.products.CreateProductDTO;
import pl.oliwawyplywa.web.dto.products.ProductDTO;
import pl.oliwawyplywa.web.dto.products.ProductOptionDTO;
import pl.oliwawyplywa.web.repositories.ProductOptionsRepository;
import pl.oliwawyplywa.web.repositories.ProductsRepository;
import pl.oliwawyplywa.web.schemas.Category;
import pl.oliwawyplywa.web.schemas.Product;
import pl.oliwawyplywa.web.schemas.ProductOption;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductOptionsRepository productOptionsRepository;
    private final CategoriesService categoriesService;

    public ProductsService(ProductsRepository productsRepository, ProductOptionsRepository productOptionsRepository, CategoriesService categoriesService) {
        this.productsRepository = productsRepository;
        this.productOptionsRepository = productOptionsRepository;
        this.categoriesService = categoriesService;
    }

    public List<Product> getProducts() {
        return productsRepository.findAll();
    }

    @Transactional
    public Product createProduct(CreateProductDTO productDTO) {
        Category category = categoriesService.getCategoryById(productDTO.getCategoryId());
        Product product = new Product(category, productDTO.getProductName(), productDTO.getProductDescription());
        productsRepository.save(product);

        createProductOptions(product, productDTO.getProductOptions());

        return product;
    }

    @Transactional
    public void createProductOptions(Product product, List<ProductOptionDTO> productOptionsDTO) {
        List<ProductOption> productOptions = productOptionsDTO.stream()
            .map(po -> new ProductOption(product, po.getOptionLabel(), po.getOptionPrice()))
            .collect(Collectors.toList());

        productOptionsRepository.saveAll(productOptions);
    }

}
