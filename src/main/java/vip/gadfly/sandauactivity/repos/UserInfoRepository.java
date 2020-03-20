package vip.gadfly.sandauactivity.repos;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    UserInfo findByOpenid(String username);
    boolean existsById(String id);
    boolean existsByOpenid(String openid);
}
