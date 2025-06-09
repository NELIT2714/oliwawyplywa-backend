package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.oliwawyplywa.web.schemas.Category;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c FROM Category c WHERE c.categoryName = :categoryName")
    Optional<Category> getCategoryByName(@Param("category_name") String categoryName);

}
