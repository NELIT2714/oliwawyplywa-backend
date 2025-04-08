package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oliwawyplywa.web.schemas.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
}
