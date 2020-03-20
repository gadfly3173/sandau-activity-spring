package vip.gadfly.sandauactivity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gadfly.sandauactivity.utils.JWTUtil;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String say() {
        String openid = "1234";
        return JWTUtil.sign(openid);
    }
}
