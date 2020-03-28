package vip.gadfly.sandauactivity.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import vip.gadfly.sandauactivity.models.Activity;

public interface ActivityRepository extends JpaRepository<Activity, String> {
}
