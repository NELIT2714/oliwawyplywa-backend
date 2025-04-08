package pl.oliwawyplywa.web.controllers;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.schemas.Category;
import pl.oliwawyplywa.web.services.CategoriesService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoriesService.getAllCategories();
    }

    @GetMapping(path = "/{id}")
    public Category getCategoryById(@PathVariable("id") int categoryId) {
        return categoriesService.getCategoryById(categoryId);
    }

    @PostMapping
    public Category createCategory(@Valid @RequestBody Category categoryDto) {
        return categoriesService.createCategory(categoryDto);
    }

    @PatchMapping(path = "/{id}")
    public Category updateCategory(@PathVariable("id") int categoryId, @RequestBody Category category) {
        return categoriesService.updateCategory(categoryId, category);
    }

    @DeleteMapping(path = "/{id}")
    public List<Category> deleteCategoryById(@PathVariable("id") int categoryId) {
        return categoriesService.deleteCategoryById(categoryId);
    }

}
