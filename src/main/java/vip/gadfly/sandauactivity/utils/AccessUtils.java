package vip.gadfly.sandauactivity.utils;

import org.springframework.stereotype.Component;
import vip.gadfly.sandauactivity.repos.UserInfoRepository;

@Component
public class AccessUtils {
    //注入实例
    private final UserInfoRepository userInfoRepository;

    public AccessUtils(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public static AccessUtils accessUtils;

    public Boolean isAdmin(String token) {
        return JWTUtil.getAccessLevel(token) == "ADMIN";
    }
}
