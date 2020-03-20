package vip.gadfly.sandauactivity;

import org.springframework.web.bind.annotation.*;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.repos.UserInfo;
import vip.gadfly.sandauactivity.repos.UserInfoRepository;

@RestController
public class UserInfoController {

    private final UserInfoRepository userInfoRepository;

    public UserInfoController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/user/info")
    public GlobalJSONResult getUserInfo (@RequestParam(value = "openid", required = true) String openid) {
        if (userInfoRepository.existsByOpenid(openid)) {
            return GlobalJSONResult.ok(userInfoRepository.findByOpenid(openid));
        } else {
            return GlobalJSONResult.errorMsg("用户id不存在，请检查！");
        }

    }

    @PostMapping("/user/update")
    public GlobalJSONResult updateUserInfo (@RequestBody UserInfo userInfo) {
        if (userInfoRepository.existsById(userInfo.getId())) {
            userInfo.setUpdateTime(String.valueOf(System.currentTimeMillis()));
        } else {
            return GlobalJSONResult.errorMsg("用户id不存在，请检查！");
        }
        userInfoRepository.save(userInfo);
        return GlobalJSONResult.ok(null, "用户信息更新成功");
    }
}
