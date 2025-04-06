package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.oliwawyplywa.web.schemas.Category;

public interface CategoriesRepository extends JpaRepository<Category, Integer> {

}
