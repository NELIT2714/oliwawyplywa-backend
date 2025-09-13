package pl.oliwawyplywa.web.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.Product;

@Repository
public interface ProductsRepository extends ReactiveCrudRepository<Product, Integer> {
}
