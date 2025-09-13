package pl.oliwawyplywa.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.schemas.Category;
import pl.oliwawyplywa.web.services.CategoriesService;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/categories")
@Tag(name = "Categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Operation(summary = "Get all categories")
    @ApiResponse(
        responseCode = "200",
        description = "Categories list",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                        {
                          "status": true,
                          "categories": [
                            {"category_id": 1, "category_name": "Test category 1"},
                            {"category_id": 2, "category_name": "Test category 2"}
                          ]
                        }
                        """
            )
        )
    )
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getCategories() {
        return categoriesService.getCategories()
            .collectList()
            .map(list -> ResponseEntity.ok(Map.of("status", true, "categories", list)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getCategory(@PathVariable("id") int categoryId) {
        return categoriesService.getCategory(categoryId)
            .map(category -> ResponseEntity.ok(Map.of("status", true, "category", category)));
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createCategory(@Valid @RequestBody Category categoryDto) {
        return categoriesService.createCategory(categoryDto)
            .map(category -> ResponseEntity.ok(Map.of("status", true, "category", category)));
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateCategory(@PathVariable("id") int categoryId,
                                                                    @Valid @RequestBody Category category) {
        return categoriesService.updateCategory(categoryId, category)
            .map(updatedCategory -> ResponseEntity.ok(Map.of("status", true, "category", updatedCategory)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteCategory(@PathVariable("id") int categoryId) {
        return categoriesService.deleteCategoryById(categoryId)
            .collectList()
            .map(list -> ResponseEntity.ok(Map.of("status", true, "categories", list)));
    }
}
