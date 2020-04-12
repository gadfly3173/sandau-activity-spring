package vip.gadfly.sandauactivity.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.pojo.LoginCode;
import vip.gadfly.sandauactivity.models.UserInfo;
import vip.gadfly.sandauactivity.repos.UserInfoRepository;
import vip.gadfly.sandauactivity.utils.QQLoginUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vip.gadfly.sandauactivity.utils.JWTUtil;

/**
 * 登录接口类
 */
@RestController
public class LoginController {

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    //注入实例
    private final UserInfoRepository userInfoRepository;
    private final RestTemplate restTemplate;

    public LoginController(UserInfoRepository userInfoRepository, RestTemplate restTemplate) {
        this.userInfoRepository = userInfoRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = "/login/callback", consumes= { MediaType.APPLICATION_JSON_VALUE})
    public GlobalJSONResult handleCallbackCode(@RequestBody LoginCode reqParams) throws JsonProcessingException {
        String authorization_code = reqParams.getCode();
        if (StringUtils.isBlank(authorization_code)) {
            return GlobalJSONResult.errorMsg("code无效，请重新授权！");
        }
        //client端的状态值。用于第三方应用防止CSRF攻击。
        String state = reqParams.getState();
        if (!state.equals("login")) {
            logger.error("client端的状态值不匹配！");
            return GlobalJSONResult.errorMsg("state无效，请确认是否为本人操作！");
        }
        String access_token = getAccessToken(authorization_code);
        if (StringUtils.isBlank(access_token)) {
            return GlobalJSONResult.errorMsg("access_token获取失败，请重新授权！");
        }
        String url = String.format("https://graph.qq.com/oauth2.0/me?access_token=%s", access_token);
        //第二次模拟客户端发出请求后得到的是带openid的返回数据,格式如下
        //callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
        String secondCallbackInfo = restTemplate.getForObject(url, String.class);
        //正则表达式处理
        String regex = "\\{.*\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(secondCallbackInfo);
        if (!matcher.find()) {
            logger.error("异常的回调值: " + secondCallbackInfo);
            return GlobalJSONResult.errorMsg("异常的回调值: " + secondCallbackInfo);
        }

        //调用jackson
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> hashMap = objectMapper.readValue(matcher.group(0), HashMap.class);

        String openid = hashMap.get("openid");

        // 获取QQ用户信息
        String user_info_url = getUserInfoUrl(access_token, openid);
        String user_result = restTemplate.getForObject(user_info_url, String.class);
        Map<String, Object> user_info_qq = objectMapper.readValue(user_result, Map.class);
        if ((int) user_info_qq.get("ret") != 0) {
            return GlobalJSONResult.errorMsg("用户信息获取失败，请重试");
        }

        String token = getToken(openid, user_info_qq);

        user_info_qq.put("token", token);
        return GlobalJSONResult.ok(user_info_qq);

    }

    private String getToken(String openid, Map<String, Object> user_info_qq) {
        String uid = UUID.nameUUIDFromBytes(openid.getBytes()).toString();
        String access_level;
        if (userInfoRepository.existsById(uid)) {
            UserInfo userInfo = userInfoRepository.findById(uid).get();
            userInfo.setNickname(user_info_qq.get("nickname").toString());
            userInfo.setAvatarUrl(user_info_qq.get("figureurl_2").toString());
            userInfoRepository.save(userInfo);
            access_level = userInfo.getAccessLevel();
        } else {
            UserInfo userInfo = new UserInfo(uid, openid, user_info_qq.get("nickname").toString(),
                    user_info_qq.get("figureurl_2").toString(), System.currentTimeMillis());
            userInfoRepository.save(userInfo);
            access_level = userInfo.getAccessLevel();
        }
        user_info_qq.put("access_level", access_level);
        return JWTUtil.sign(openid, access_level);
    }

    private String getAccessToken(String authorization_code) {
        String urlForAccessToken = getUrlForAccessToken(authorization_code);
        String firstCallbackInfo = restTemplate.getForObject(urlForAccessToken, String.class);
        String[] params = firstCallbackInfo.split("&");
        String access_token = null;
        for (String param : params) {
            String[] keyvalue = param.split("=");
            if (keyvalue[0].equals("access_token")) {
                access_token = keyvalue[1];
                break;
            }
        }
        return access_token;
    }

    private static String getUrlForAccessToken(String authorization_code) {
        String grant_type = "authorization_code";
        String client_id = QQLoginUtil.getQQLoginClientId();
        String client_secret = QQLoginUtil.getQQLoginClientSecret();
        String redirect_uri = QQLoginUtil.getQQLoginRedirectUri();

        String url = String.format("https://graph.qq.com/oauth2.0/token" +
                        "?grant_type=%s&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s",
                grant_type, client_id, client_secret, authorization_code, redirect_uri);

        return url;
    }

    private static String getUserInfoUrl(String access_token, String openid) {
        String client_id = QQLoginUtil.getQQLoginClientId();
        String url = String.format("https://graph.qq.com/user/get_user_info" +
                "?access_token=%s&oauth_consumer_key=%s&openid=%s", access_token, client_id, openid);
        return url;
    }

}
