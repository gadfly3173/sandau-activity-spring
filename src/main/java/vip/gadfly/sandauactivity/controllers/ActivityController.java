package vip.gadfly.sandauactivity.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.sandauactivity.models.Activity;
import vip.gadfly.sandauactivity.models.Categories;
import vip.gadfly.sandauactivity.models.UserInfo;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.repos.ActivityRepository;
import vip.gadfly.sandauactivity.utils.AccessUtils;
import vip.gadfly.sandauactivity.utils.JWTUtil;

import java.util.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final AccessUtils accessUtils;

    public ActivityController(AccessUtils accessUtils, ActivityRepository activityRepository) {
        //注入实例
        this.activityRepository = activityRepository;
        this.accessUtils = accessUtils;
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE})
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
        UserInfo userInfo = new UserInfo(userId);
        Categories category = new Categories(reqActivity.getCategories().getId());
        reqActivity.setUserInfo(userInfo);
//        Categories setCategory = categoriesRepository.findById(reqActivity.getCategories().getId());
//        if (setCategory == null) {
//            setCategory = categoriesRepository.findById(1);
//        }
        reqActivity.setCategories(category);
        activityRepository.save(reqActivity);

        return GlobalJSONResult.ok();
    }

    @GetMapping("/list")
    public GlobalJSONResult queryActivity(@RequestParam String title, @RequestParam int pageNum, @RequestParam int pageSize) {
        if (StringUtils.isBlank(title)) {
            return GlobalJSONResult.ok(getNoTitleResultData(pageSize), "活动查询成功");
        }

        return GlobalJSONResult.ok(getResultData(title, pageNum, pageSize), "活动查询成功");
    }

    @GetMapping("/list/bycat")
    public GlobalJSONResult queryActivityByCat(@RequestParam Integer catid, @RequestParam int pageNum, @RequestParam int pageSize) {
        if (catid == null) {
            return GlobalJSONResult.errorMsg("分类id不得为空");
        }

        return GlobalJSONResult.ok(getResultDataByCat(catid, pageNum, pageSize), "活动查询成功");
    }

    @GetMapping("/actdetail")
    public GlobalJSONResult getActivityDetail(@RequestParam String id) {
        if (StringUtils.isBlank(id)) {
            return GlobalJSONResult.errorMsg("id不得为空！");
        }
        Activity activity = activityRepository.findById(id).orElse(null);
        if (activity == null || StringUtils.isBlank(activity.toString())) {
            return GlobalJSONResult.errorMsg("活动查询失败！请检查提交参数！");
        }
        HashMap<String, Object> act = new HashMap<>();
        act.put("id", activity.getId());
        act.put("title", activity.getTitle());
        act.put("brief", activity.getBrief());
        act.put("content", activity.getContent());
        act.put("actStartTime", activity.getActStartTime());
        act.put("actEndTime", activity.getActEndTime());
        act.put("regStartTime", activity.getRegStartTime());
        act.put("regEndTime", activity.getRegEndTime());
        act.put("img", activity.getImg());
        act.put("registered", activity.getRegistered());
        act.put("maxMembers", activity.getMaxMembers());
        act.put("location", activity.getLocation());
        act.put("categories", activity.getCategories());
        UserInfo user = activity.getUserInfo();
        act.put("user", StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getNickname());
        return GlobalJSONResult.ok(act, "活动查询成功");
    }

    private Map<String, Object> getNoTitleResultData(int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<Activity> pageActivity = activityRepository.findAll(pageable);
        return getResultDataFromPage(pageActivity, pageActivity.getNumber() + 1, pageActivity.getSize());
    }


    private Map<String, Object> getResultData(String title, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Activity> pageActivity = activityRepository.findByTitleLikeOrBriefLike("%" + title + "%",
                "%" + title + "%", pageable);
        return getResultDataFromPage(pageActivity, pageNum, pageSize);
    }

    private Map<String, Object> getResultDataByCat(Integer catid, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Categories category = new Categories(catid);
        Page<Activity> pageActivity = activityRepository.findByCategories(category, pageable);
        return getResultDataFromPage(pageActivity, pageActivity.getNumber() + 1, pageActivity.getSize());
    }

    private Map<String, Object> getResultDataFromPage(Page<Activity> pageActivity, int i, int size) {
        List<Activity> content = pageActivity.getContent();
        Integer totalPages = pageActivity.getTotalPages();
        Long totalActs = pageActivity.getTotalElements();
        List<HashMap> contentData = new ArrayList<>();

        for (Activity element : content) {
            HashMap<String, Object> act = new HashMap<>();
            act.put("id", element.getId());
            act.put("title", element.getTitle());
            act.put("brief", element.getBrief());
            act.put("actStartTime", element.getActStartTime());
            act.put("actEndTime", element.getActEndTime());
            act.put("regStartTime", element.getRegStartTime());
            act.put("regEndTime", element.getRegEndTime());
            act.put("img", element.getImg());
            act.put("registered", element.getRegistered());
            act.put("maxMembers", element.getMaxMembers());
            act.put("location", element.getLocation());
            act.put("categories", element.getCategories());
            contentData.add(act);
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("content", contentData);
        resultData.put("pageNum", i);
        resultData.put("pageSize", size);
        resultData.put("totalPages", totalPages);
        resultData.put("totalActs", totalActs);
        return resultData;
    }
}
