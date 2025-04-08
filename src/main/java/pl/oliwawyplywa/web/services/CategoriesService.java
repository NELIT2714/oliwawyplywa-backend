package pl.oliwawyplywa.web.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.CategoriesRepository;
import pl.oliwawyplywa.web.schemas.Category;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Category> getAllCategories() {
        return categoriesRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoriesRepository.findById(id).orElseThrow(() -> new HTTPException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public Category createCategory(Category categoryDto) {
        Optional<Category> category = categoriesRepository.getCategoryByName(categoryDto.getCategoryName());
        if (category.isPresent()) {
            throw new HTTPException(HttpStatus.CONFLICT, "Category already exists");
        }
        return categoriesRepository.save(categoryDto);
    }

    public Category updateCategory(int categoryId, Category categoryDto) {
        Category category = getCategoryById(categoryId);

        if (category.getCategoryName().equals(categoryDto.getCategoryName().trim())) {
            throw new HTTPException(HttpStatus.CONFLICT, "Category name not changed");
        }

        Optional<Category> optionalCategory = categoriesRepository.getCategoryByName(categoryDto.getCategoryName());
        if (optionalCategory.isPresent()) {
            throw new HTTPException(HttpStatus.CONFLICT, "Category already exists");
        }
        category.setCategoryName(categoryDto.getCategoryName().trim());
        return categoriesRepository.save(category);
    }

    public List<Category> deleteCategoryById(int categoryId) {
        categoriesRepository.delete(getCategoryById(categoryId));
        return categoriesRepository.findAll();
    }

}
