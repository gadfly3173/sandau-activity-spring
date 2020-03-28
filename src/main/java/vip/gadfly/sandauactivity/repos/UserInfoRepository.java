package vip.gadfly.sandauactivity.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import vip.gadfly.sandauactivity.models.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    UserInfo findByOpenid(String username);
    boolean existsById(String id);
    boolean existsByOpenid(String openid);
    boolean existsByPhone(Long phone);
    boolean existsByStuNum(String stuNum);
}
