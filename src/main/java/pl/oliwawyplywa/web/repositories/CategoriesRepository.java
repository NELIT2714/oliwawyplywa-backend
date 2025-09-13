package pl.oliwawyplywa.web.repositories;

import org.springframework.stereotype.Repository;
import pl.oliwawyplywa.web.schemas.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

@Repository
public interface CategoriesRepository extends ReactiveCrudRepository<Category, Integer> {

    Mono<Category> findByCategoryName(String categoryName);

}
