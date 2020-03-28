package vip.gadfly.sandauactivity.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.models.UserInfo;
import vip.gadfly.sandauactivity.repos.UserInfoRepository;
import vip.gadfly.sandauactivity.utils.JWTUtil;

import java.util.UUID;

@RestController
public class UserInfoController {

    private final UserInfoRepository userInfoRepository;

    public UserInfoController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/user/info")
    public GlobalJSONResult getUserInfo(@RequestHeader(value = "X-token") String token) {
        String openid = JWTUtil.getOpenid(token);
        if (userInfoRepository.existsByOpenid(openid)) {
            return GlobalJSONResult.ok(userInfoRepository.findByOpenid(openid), "获取成功");
        }
        return GlobalJSONResult.errorMsg("用户id不存在，请检查！");


    }

    @PostMapping("/user/update")
    public GlobalJSONResult updateUserInfo(@RequestBody UserInfo userInfoReq, @RequestHeader(value = "X-token") String token) {
        if (!userInfoReq.getId().equals(UUID.nameUUIDFromBytes(JWTUtil.getOpenid(token).getBytes()).toString())) {
            System.out.println(userInfoReq.getId());
            System.out.println(UUID.nameUUIDFromBytes(JWTUtil.getOpenid(token).getBytes()).toString());
            return GlobalJSONResult.errorMsg("非法id，请检查！");
        }

        if (!userInfoRepository.existsById(userInfoReq.getId())) {
            return GlobalJSONResult.errorMsg("用户id不存在，请检查！");
        }

        UserInfo userInfo = userInfoRepository.findById(userInfoReq.getId()).get();

        if (StringUtils.isNotBlank(String.valueOf(userInfoReq.getPhone())) && !userInfo.getPhone().equals(userInfoReq.getPhone())) {
            if (!String.valueOf(userInfoReq.getPhone()).matches("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$") || String.valueOf(userInfoReq.getPhone()).length() != 11) {
                return GlobalJSONResult.errorMsg("手机号不正确！");
            }
            if (userInfoRepository.existsByPhone(userInfoReq.getPhone())) {
                return GlobalJSONResult.errorMsg("手机号已被登记！");
            }
        }
        if (StringUtils.isNotBlank(userInfoReq.getStuNum()) && !userInfo.getStuNum().equals(userInfoReq.getStuNum())) {
            if (userInfoRepository.existsByStuNum(userInfoReq.getStuNum())) {
                return GlobalJSONResult.errorMsg("学号已被登记！");
            }
        }
        userInfo.setStuNum(userInfoReq.getStuNum());
        userInfo.setName(userInfoReq.getName());
        userInfo.setPhone(userInfoReq.getPhone());
        userInfo.setUpdateTime(System.currentTimeMillis());

        userInfoRepository.save(userInfo);
        return GlobalJSONResult.ok(null, "用户信息更新成功");
    }
}
