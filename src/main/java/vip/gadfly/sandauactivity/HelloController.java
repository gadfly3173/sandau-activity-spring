package vip.gadfly.sandauactivity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
//
//    private final CategoriesRepository categoriesRepository;
//
//    public HelloController(CategoriesRepository categoriesRepository) {
//        this.categoriesRepository = categoriesRepository;
//    }

    @GetMapping("/hello")
    public String say() {
//        Categories category = new Categories("其他", System.currentTimeMillis());
//        categoriesRepository.save(category);
        return "complete";
    }
}
