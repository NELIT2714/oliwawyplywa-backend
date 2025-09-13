package pl.oliwawyplywa.web.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.exceptions.HTTPException;
import pl.oliwawyplywa.web.repositories.CategoriesRepository;
import pl.oliwawyplywa.web.schemas.Category;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public Flux<Category> getCategories() {
        return categoriesRepository.findAll();
    }

    public Mono<Category> getCategory(int id) {
        return categoriesRepository.findById(id)
            .switchIfEmpty(Mono.error(new HTTPException(HttpStatus.NOT_FOUND,
                "Kategoria nie została znaleziona w systemie. Sprawdź poprawność nazwy i spróbuj ponownie")));
    }

    public Mono<Category> createCategory(Category categoryDto) {
        return categoriesRepository.findByCategoryName(categoryDto.getCategoryName().trim())
            .flatMap(existing -> Mono.<Category>error(new HTTPException(HttpStatus.NOT_FOUND,
                "Kategoria o podanej nazwie już istnieje w systemie. Spróbuj użyć unikalnej nazwy dla kategorii")))
            .switchIfEmpty(
                categoriesRepository.save(new Category(categoryDto.getCategoryName().trim()))
            );
    }

    public Mono<Category> updateCategory(int categoryId, Category categoryDto) {
        return getCategory(categoryId)
            .flatMap(category -> {
                if (category.getCategoryName().equals(categoryDto.getCategoryName().trim())) {
                    return Mono.error(new HTTPException(HttpStatus.NOT_FOUND,
                        "Nazwa kategorii nie została zmieniona. Sprawdź poprawność danych i spróbuj ponownie"));
                }
                return categoriesRepository.findByCategoryName(categoryDto.getCategoryName().trim())
                    .flatMap(existing -> Mono.<Category>error(new HTTPException(HttpStatus.NOT_FOUND,
                        "Kategoria o podanej nazwie już istnieje w systemie. Spróbuj użyć unikalnej nazwy dla kategorii")))
                    .switchIfEmpty(Mono.defer(() -> {
                        category.setCategoryName(categoryDto.getCategoryName().trim());
                        return categoriesRepository.save(category);
                    }));
            });
    }

    public Flux<Category> deleteCategoryById(int categoryId) {
        return getCategory(categoryId)
            .flatMapMany(category -> categoriesRepository.delete(category).thenMany(categoriesRepository.findAll()));
    }

}
