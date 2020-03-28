package vip.gadfly.sandauactivity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.repos.CategoriesRepository;

@RestController
public class CategoriesController {
    private final CategoriesRepository categoriesRepository;

    public CategoriesController(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @GetMapping("/categories/list")
    public GlobalJSONResult getCategoriesList() {
        return GlobalJSONResult.ok(categoriesRepository.findAll());
    }
}
