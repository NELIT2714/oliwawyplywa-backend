package pl.oliwawyplywa.web.utils.mappers;

import org.springframework.stereotype.Component;
import pl.oliwawyplywa.web.dto.products.ResponseProductDTO;
import pl.oliwawyplywa.web.dto.products.ResponseProductOptionDTO;
import pl.oliwawyplywa.web.schemas.Product;
import pl.oliwawyplywa.web.schemas.ProductOption;

import java.util.List;

@Component
public class ProductMapper {

    public List<ResponseProductOptionDTO> mapProductOptionsToDTO(Product product) {
        List<ProductOption> productOptions = product.getProductOptions();
        return productOptions.stream()
            .map(po -> new ResponseProductOptionDTO(
                po.getOptionLabel(), po.getOptionPrice(), po.getIdProductOption()
            )).toList();
    }

    public ResponseProductDTO mapProductToDTO(Product product) {
        return new ResponseProductDTO(
            product.getIdProduct(), product.getCategory().getIdCategory(), product.getProductName(),
            product.getProductDescription(), mapProductOptionsToDTO(product)
        );
    }

}
