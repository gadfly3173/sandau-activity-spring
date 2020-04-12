package vip.gadfly.sandauactivity.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vip.gadfly.sandauactivity.models.Activity;
import vip.gadfly.sandauactivity.models.SignUp;
import vip.gadfly.sandauactivity.models.UserInfo;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.repos.ActivityRepository;
import vip.gadfly.sandauactivity.repos.SignUpRepository;
import vip.gadfly.sandauactivity.utils.JWTUtil;

import java.util.*;

@RestController
public class SignUpController {

    private final SignUpRepository signUpRepository;
    private final ActivityRepository activityRepository;

    public SignUpController(SignUpRepository signUpRepository, ActivityRepository activityRepository) {
        this.signUpRepository = signUpRepository;
        this.activityRepository = activityRepository;
    }

    @PostMapping("/signup/sign")
    @Transactional( rollbackFor = Exception.class )
    public GlobalJSONResult userSignUpActivity(@RequestBody HashMap<String,String> reqBody,
                                               @RequestHeader(value = "X-token") String token) {
        String activity_id = reqBody.get("activity");
        String contact = reqBody.get("contact");
        String openid = JWTUtil.getOpenid(token);
        Activity activity = activityRepository.findById(activity_id).orElse(null);
        if (activity == null) {
            return GlobalJSONResult.errorMsg("活动不存在");
        }
        UserInfo userInfo = new UserInfo(UUID.nameUUIDFromBytes(openid.getBytes()).toString());
        SignUp signUp = new SignUp(contact, System.currentTimeMillis(), activity, userInfo);

        transactionalSignUp(activity, signUp, userInfo);

        return GlobalJSONResult.ok("报名成功！", "报名成功！");
    }

    @GetMapping("/signup/user")
    public GlobalJSONResult getUserRegistered(@RequestHeader(value = "X-token") String token, @RequestParam int pageNum, @RequestParam int pageSize) {
        String openid = JWTUtil.getOpenid(token);
        UserInfo userInfo = new UserInfo(UUID.nameUUIDFromBytes(openid.getBytes()).toString());

        return GlobalJSONResult.ok(getUserSignList(userInfo, pageNum, pageSize), "已报名活动查询成功");
    }

    @PostMapping("/signup/cancelsign")
    @Transactional( rollbackFor = Exception.class )
    public GlobalJSONResult userCancelSignUpActivity(@RequestBody HashMap<String,String> reqBody,
                                               @RequestHeader(value = "X-token") String token) {
        String activity_id = reqBody.get("activity");
        String openid = JWTUtil.getOpenid(token);
        Activity activity = activityRepository.findById(activity_id).orElse(null);
        if (activity == null) {
            return GlobalJSONResult.errorMsg("活动不存在！");
        }
        UserInfo userInfo = new UserInfo(UUID.nameUUIDFromBytes(openid.getBytes()).toString());
        SignUp signUp = signUpRepository.findByUserInfoAndActivity(userInfo, activity);
        if (signUp == null) {
            return GlobalJSONResult.errorMsg("未报名！");
        }
        if (signUp.getStatus().equals("CANCEL")) {
            return GlobalJSONResult.errorMsg("已取消！不得重复！");
        }

        transactionalCancelSignUp(activity, signUp);

        return GlobalJSONResult.ok("报名取消成功！", "报名取消成功！");
    }

    private void transactionalSignUp(Activity activity, SignUp signUp, UserInfo userInfo) {
        if (activity.getRegStartTime() > System.currentTimeMillis() || activity.getRegEndTime() < System.currentTimeMillis()) {
            throw new RuntimeException("不在报名时间内！");
        }
        if (signUpRepository.existsByActivityAndUserInfo(activity, userInfo)) {
            throw new RuntimeException("不准重复报名！");
        }
        if (activity.getMaxMembers() <= activity.getRegistered()) {
            throw new RuntimeException("活动报名人数已达上限！");
        }
        signUpRepository.save(signUp);
        activity.setRegistered(activity.getRegistered() + 1);
        activity.setUpdateTime(System.currentTimeMillis());
        activityRepository.save(activity);
    }

    private void transactionalCancelSignUp(Activity activity, SignUp signUp) throws RuntimeException {
        if (activity.getRegStartTime() > System.currentTimeMillis() || activity.getRegEndTime() < System.currentTimeMillis()) {
            throw new RuntimeException("不在报名时间内！不得取消！");
        }
        signUp.setStatus("CANCEL");
        signUp.setUpdateTime(System.currentTimeMillis());
        signUpRepository.save(signUp);
        activity.setRegistered(activity.getRegistered() - 1);
        activity.setUpdateTime(System.currentTimeMillis());
        activityRepository.save(activity);
    }

    private Map<String, Object> getUserSignList(UserInfo userInfo, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<SignUp> pageSignUp = signUpRepository.findAllByUserInfo(userInfo, pageable);
        return getUserSignListContent(pageSignUp, pageNum, pageSize);
    }

    private Map<String, Object> getUserSignListContent(Page<SignUp> pageSignUp, int i, int size) {
        List<SignUp> content = pageSignUp.getContent();
        Integer totalPages = pageSignUp.getTotalPages();
        Long totalActs = pageSignUp.getTotalElements();

        for (SignUp element : content) {
            Activity act = element.getActivity();
            act.setUserInfo(null);
            element.setActivity(act);
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("content", content);
        resultData.put("pageNum", i);
        resultData.put("pageSize", size);
        resultData.put("totalPages", totalPages);
        resultData.put("totalActs", totalActs);
        return resultData;
    }
}
