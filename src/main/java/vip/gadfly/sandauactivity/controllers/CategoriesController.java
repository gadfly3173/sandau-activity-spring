package vip.gadfly.sandauactivity.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.sandauactivity.models.Categories;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.repos.CategoriesRepository;
import vip.gadfly.sandauactivity.utils.AccessUtils;

@RestController
public class CategoriesController {
    private final AccessUtils accessUtils;
    private final CategoriesRepository categoriesRepository;

    public CategoriesController(CategoriesRepository categoriesRepository, AccessUtils accessUtils) {
        this.categoriesRepository = categoriesRepository;
        this.accessUtils = accessUtils;
    }

    @GetMapping("/categories/list")
    public GlobalJSONResult getCategoriesList() {
        return GlobalJSONResult.ok(categoriesRepository.findAll());
    }

    @PostMapping(value = "/categories/create", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public GlobalJSONResult createCategory(@RequestBody Categories category, @RequestHeader(value = "X-token") String token) {
        if (!accessUtils.isAdmin(token)) {
            return GlobalJSONResult.errorMsg("权限不足，无法新增分类");
        }
        Categories categories = new Categories(category.getCategory(), System.currentTimeMillis());
        categoriesRepository.save(categories);
        return GlobalJSONResult.ok(categories);
    }
}
