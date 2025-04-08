package pl.oliwawyplywa.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.oliwawyplywa.web.schemas.Category;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT * FROM tbl_categories WHERE category_name = :category_name", nativeQuery = true)
    Optional<Category> getCategoryByName(@Param("category_name") String categoryName);

}
