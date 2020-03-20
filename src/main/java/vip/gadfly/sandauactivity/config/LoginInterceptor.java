package vip.gadfly.sandauactivity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import vip.gadfly.sandauactivity.pojo.GlobalJSONResult;
import vip.gadfly.sandauactivity.utils.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-token");
        if ("".equals(token) || token == null || !JWTUtil.verify(token, JWTUtil.getOpenid(token)))  {
            returnJson(response, GlobalJSONResult.errorTokenMsg("登录信息无效，请重新登录！"));
            return false;
        }
        return true;
    }


    private void returnJson(HttpServletResponse response, Object json) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String strJson = mapper.writeValueAsString(json);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.write(strJson);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
