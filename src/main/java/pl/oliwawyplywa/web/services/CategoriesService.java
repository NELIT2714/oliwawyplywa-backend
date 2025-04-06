package pl.oliwawyplywa.web.services;

import org.springframework.stereotype.Service;
import pl.oliwawyplywa.web.repositories.CategoriesRepository;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }


}
