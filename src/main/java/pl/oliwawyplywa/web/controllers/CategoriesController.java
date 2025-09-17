package pl.oliwawyplywa.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oliwawyplywa.web.dto.categories.EditCategoryDTO;
import pl.oliwawyplywa.web.schemas.Category;
import pl.oliwawyplywa.web.services.CategoriesService;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/categories")
@Tag(name = "Categories")
@Validated
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Operation(summary = "Get categories list")
    @ApiResponse(
        responseCode = "200",
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

    @Operation(summary = "Get category by ID")
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                        {
                          "category": {
                            "category_id": 1,
                            "category_name": "Category name"
                          },
                          "status": true
                        }
                        """
            )
        )
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getCategory(@PathVariable("id") int categoryId) {
        return categoriesService.getCategory(categoryId)
            .map(category -> ResponseEntity.ok(Map.of("status", true, "category", category)));
    }

    @Operation(
        summary = "Create category",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Category.class),
                examples = {
                    @ExampleObject(
                        name = "Create example category",
                        value =
                            """
                                {
                                    "category_name": "Category name"
                                }
                                """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                        {
                          "category": {
                            "category_id": 1,
                            "category_name": "Category name"
                          },
                          "status": true
                        }
                        """
            )
        )
    )
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> createCategory(@Valid @RequestBody Category categoryDto) {
        return categoriesService.createCategory(categoryDto)
            .map(category -> ResponseEntity.ok(Map.of("status", true, "category", category)));
    }

    @Operation(
        summary = "Update category by ID",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Category.class),
                examples = {
                    @ExampleObject(
                        name = "Update example category",
                        value =
                            """
                                {
                                    "category_name": "New category name"
                                }
                                """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                        {
                            "status": true,
                            "categories": [
                                {"category_id": 1, "category_name": "New category name"}
                            ]
                        }
                        """
            )
        )
    )
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateCategory(@PathVariable("id") int categoryId,
                                                                    @Valid @RequestBody EditCategoryDTO category) {
        return categoriesService.updateCategory(categoryId, category)
            .map(updatedCategory -> ResponseEntity.ok(Map.of("status", true, "category", updatedCategory)));
    }

    @Operation(summary = "Delete category by ID")
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example =
                    """
                        {
                            "status": true,
                            "categories": [
                                {"category_id": 1, "category_name": "Test category 1"}
                            ]
                        }
                        """
            )
        )
    )
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteCategory(@PathVariable("id") int categoryId) {
        return categoriesService.deleteCategoryById(categoryId)
            .collectList()
            .map(list -> ResponseEntity.ok(Map.of("status", true, "categories", list)));
    }
}
