package vip.gadfly.sandauactivity.utils;

import org.springframework.stereotype.Component;

@Component
public class AccessUtils {
    public Boolean isAdmin(String token) {
        return JWTUtil.getAccessLevel(token).equals("ADMIN");
    }
}
