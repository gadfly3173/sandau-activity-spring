package vip.gadfly.sandauactivity.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.sandauactivity.models.Categories;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.models.Activity;
import vip.gadfly.sandauactivity.repos.ActivityRepository;
import vip.gadfly.sandauactivity.repos.CategoriesRepository;
import vip.gadfly.sandauactivity.repos.UserInfoRepository;
import vip.gadfly.sandauactivity.utils.AccessUtils;
import vip.gadfly.sandauactivity.utils.JWTUtil;

import java.util.UUID;

@RestController
public class ActivityController {

    //注入实例
    private final UserInfoRepository userInfoRepository;
    private final CategoriesRepository categoriesRepository;
    private final ActivityRepository activityRepository;
    private final AccessUtils accessUtils;

    public ActivityController(UserInfoRepository userInfoRepository, AccessUtils accessUtils,
                              CategoriesRepository categoriesRepository, ActivityRepository activityRepository) {
        this.userInfoRepository = userInfoRepository;
        this.categoriesRepository = categoriesRepository;
        this.activityRepository = activityRepository;
        this.accessUtils = accessUtils;
    }

    @PostMapping("/activity/create")
    public GlobalJSONResult createActivity(@RequestBody Activity reqActivity, @RequestHeader(value = "X-token") String token) {
        String userId = UUID.nameUUIDFromBytes(JWTUtil.getOpenid(token).getBytes()).toString();
        if (!accessUtils.isAdmin(token)) {
            return GlobalJSONResult.errorMsg("权限不足，无法发布活动");
        }
        if (StringUtils.isBlank(reqActivity.getBrief()) || StringUtils.isBlank(reqActivity.getTitle()) ||
                StringUtils.isBlank(reqActivity.getContent()) || StringUtils.isBlank(String.valueOf(reqActivity.getMaxMembers())) ||
                reqActivity.getMaxMembers() < 0) {
            return GlobalJSONResult.errorMsg("请勿提交非法参数！");
        }
        reqActivity.setCreateTime(System.currentTimeMillis());
        reqActivity.setUserInfo(userInfoRepository.findById(userId).orElse(null));
        Categories setCategory = categoriesRepository.findById(reqActivity.getCategories().getId());
        if (setCategory == null) {
            setCategory = categoriesRepository.findById(1);
        }
        reqActivity.setCategories(setCategory);
        activityRepository.save(reqActivity);

        return GlobalJSONResult.ok();
    }

//    @GetMapping("/activity/list")
//    public GlobalJSONResult queryActivity()
}
