package vip.gadfly.sandauactivity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String say() {
        return UUID.nameUUIDFromBytes(("openid" + System.currentTimeMillis()).getBytes()).toString();
    }
}
