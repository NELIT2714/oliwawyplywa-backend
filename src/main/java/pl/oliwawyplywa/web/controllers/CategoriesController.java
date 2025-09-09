package pl.oliwawyplywa.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.categories.CreateCategoryDTO;
import pl.oliwawyplywa.web.schemas.Category;
import pl.oliwawyplywa.web.services.CategoriesService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/categories")
@Validated
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCategories() {
        List<Category> categories = categoriesService.getCategories();
        return ResponseEntity.ok().body(Map.of("status", true, "categories", categories));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Map<String, Object>> getCategory(@PathVariable("id") int categoryId) {
        Category category = categoriesService.getCategory(categoryId);
        return ResponseEntity.ok().body(Map.of("status", true, "category", category));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody Category categoryDto) {
        Category category = categoriesService.createCategory(categoryDto);
        return ResponseEntity.ok().body(Map.of("status", true, "category", category));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable("id") int categoryId, @RequestBody Category category) {
        Category updatedCategory = categoriesService.updateCategory(categoryId, category);
        return ResponseEntity.ok().body(Map.of("status", true, "category", updatedCategory));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable("id") int categoryId) {
        List<Category> categories = categoriesService.deleteCategoryById(categoryId);
        return ResponseEntity.ok().body(Map.of("status", true, "categories", categories));
    }

}
