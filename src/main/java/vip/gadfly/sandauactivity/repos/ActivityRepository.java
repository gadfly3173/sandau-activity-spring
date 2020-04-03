package vip.gadfly.sandauactivity.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vip.gadfly.sandauactivity.models.Activity;

public interface ActivityRepository extends JpaRepository<Activity, String> {
    Page<Activity> findAll(Pageable pageable);
    Page<Activity> findByTitleLikeOrBriefLike(String title, String brief, Pageable pageable);
}
