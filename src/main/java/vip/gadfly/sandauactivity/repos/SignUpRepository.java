package vip.gadfly.sandauactivity.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vip.gadfly.sandauactivity.models.Activity;
import vip.gadfly.sandauactivity.models.SignUp;
import vip.gadfly.sandauactivity.models.UserInfo;

public interface SignUpRepository extends JpaRepository<SignUp,String> {
    boolean existsByActivityAndUserInfo(Activity activity, UserInfo userInfo);
    Page<SignUp> findAllByUserInfo(UserInfo userInfo, Pageable pageable);
    SignUp findByUserInfoAndActivity(UserInfo userInfo, Activity activity);
}
