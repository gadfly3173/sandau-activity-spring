package vip.gadfly.sandauactivity.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JWTUtil {

    private final static Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    @Autowired
    private static RedisTemplate<Object, Object> stringRedisTemplate;

    // 过期时间7天
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 1000;

    /**
     * 生成签名,7days后过期
     *
     * @param openid qq_openid
     * @param secret uid
     * @return 加密的token
     */
    public static String sign(String openid, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(openid, uuid, EXPIRE_TIME, TimeUnit.MILLISECONDS);
        logger.info(openid + "&" + uuid);

        // 附带openid信息
        return JWT.create()
                .withClaim("openid", openid + "&" + uuid)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String openid, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("openid", openid)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的openid
     */
    public static String getOpenid(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("openid").asString();
    }

}
