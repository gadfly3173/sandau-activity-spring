package vip.gadfly.sandauactivity.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtil {

    private final static Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    private final RedisUtils nonStaticRedisUtils;

    private static RedisUtils redisUtils;

    public JWTUtil(RedisUtils nonStaticRedisUtils) {
        this.nonStaticRedisUtils = nonStaticRedisUtils;
    }

    @PostConstruct
    public void init() {
        redisUtils = nonStaticRedisUtils;
    }

    // 过期时间7天
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 生成签名,7days后过期
     *
     * @param openid qq_openid
     * @return 加密的token
     */
    public static String sign(String openid, String access_level) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        if (redisUtils.hasKey(openid)) {
            redisUtils.del(openid);
        }
        String secret = UUID.randomUUID().toString();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        redisUtils.set(openid, secret, EXPIRE_TIME);
        logger.info(openid + "&" + secret);

        // 附带openid信息
        return JWT.create()
                .withClaim("openid", openid)
                .withClaim("access_level", access_level)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String openid, String access_level) {
        try {
            String secret = redisUtils.get(openid);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("openid", openid)
                    .withClaim("access_level", access_level)
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
    public static String getAccessLevel(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("access_level").asString();
    }

}
