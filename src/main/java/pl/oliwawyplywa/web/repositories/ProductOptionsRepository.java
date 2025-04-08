package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oliwawyplywa.web.schemas.ProductOption;

public interface ProductOptionsRepository extends JpaRepository<ProductOption, Integer> {
}
