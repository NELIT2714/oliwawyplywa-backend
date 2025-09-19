package pl.oliwawyplywa.web.utils.mappers;

import org.springframework.stereotype.Component;
import pl.oliwawyplywa.web.dto.products.ResponseProductDTO;
import pl.oliwawyplywa.web.dto.products.ResponseProductOptionDTO;
import pl.oliwawyplywa.web.repositories.ProductOptionsRepository;
import pl.oliwawyplywa.web.schemas.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductMapper {

    private final ProductOptionsRepository productOptionsRepository;

    public ProductMapper(ProductOptionsRepository productOptionsRepository) {
        this.productOptionsRepository = productOptionsRepository;
    }

    public Flux<ResponseProductOptionDTO> mapProductOptionsToDTO(Product product) {
        return productOptionsRepository.findByProductId(product.getIdProduct())
            .map(po -> new ResponseProductOptionDTO(
                po.getOptionLabel(), po.getOptionPrice(), po.getIdProductOption()
            ));
    }

    public Mono<ResponseProductDTO> mapProductToDTO(Product product) {
        return mapProductOptionsToDTO(product)
            .collectList()
            .map(optionsList -> new ResponseProductDTO(
                product.getIdProduct(),
                product.getCategoryId(),
                product.getProductName(),
                product.getProductDescription(),
                product.getProductImage(),
                optionsList
            ));
    }
}
