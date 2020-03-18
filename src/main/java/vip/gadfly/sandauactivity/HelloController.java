package vip.gadfly.sandauactivity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gadfly.sandauactivity.utils.JWTUtil;
import vip.gadfly.sandauactivity.utils.RedisUtils;

import java.util.Date;
import java.util.UUID;

@RestController
public class HelloController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/hello")
    public String say() {
        String openid = "1234";
        return JWTUtil.sign(openid);
    }
}
