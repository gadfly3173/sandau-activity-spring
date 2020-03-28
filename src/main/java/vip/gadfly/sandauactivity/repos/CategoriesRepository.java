package vip.gadfly.sandauactivity.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import vip.gadfly.sandauactivity.models.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, String> {

    Categories findById(Integer id);
}
