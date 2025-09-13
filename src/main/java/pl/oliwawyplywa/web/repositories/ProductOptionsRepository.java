package pl.oliwawyplywa.web.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.ProductOption;
import reactor.core.publisher.Flux;

@Repository
public interface ProductOptionsRepository extends ReactiveCrudRepository<ProductOption, Integer> {

    Flux<ProductOption> findByProductId(Integer productId);

}
