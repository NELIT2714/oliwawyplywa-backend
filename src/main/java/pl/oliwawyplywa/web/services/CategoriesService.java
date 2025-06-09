package pl.oliwawyplywa.web.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.CategoriesRepository;
import pl.oliwawyplywa.web.schemas.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Category> getCategories() {
        return categoriesRepository.findAll();
    }

    public Category getCategory(int id) {
        return categoriesRepository.findById(id).orElseThrow(
            () -> new HTTPException("Kategoria o podanej nazwie nie została znaleziona w systemie. Sprawdź poprawność nazwy i spróbuj ponownie")
        );
    }

    @Transactional
    public Category createCategory(Category categoryDto) {
        Optional<Category> categoryOptional = categoriesRepository.getCategoryByName(categoryDto.getCategoryName());
        if (categoryOptional.isPresent())
            throw new HTTPException("Kategoria o podanej nazwie już istnieje w systemie. Spróbuj użyć unikalnej nazwy dla kategorii");
        return categoriesRepository.save(categoryDto);
    }

    public Category updateCategory(int categoryId, Category categoryDto) {
        Category category = getCategory(categoryId);

        if (category.getCategoryName().equals(categoryDto.getCategoryName().trim())) {
            throw new HTTPException("Nazwa kategorii nie została zmieniona. Sprawdź poprawność danych i spróbuj ponownie");
        }

        Optional<Category> optionalCategory = categoriesRepository.getCategoryByName(categoryDto.getCategoryName());
        if (optionalCategory.isPresent())
            throw new HTTPException("Kategoria o podanej nazwie już istnieje w systemie. Spróbuj użyć unikalnej nazwy dla kategorii");

        category.setCategoryName(categoryDto.getCategoryName().trim());
        return categoriesRepository.save(category);
    }

    public List<Category> deleteCategoryById(int categoryId) {
        Category category = getCategory(categoryId);

        categoriesRepository.delete(category);
        return categoriesRepository.findAll();
    }

}
