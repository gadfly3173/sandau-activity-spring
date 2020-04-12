package vip.gadfly.sandauactivity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomWebConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/login/**");
    }

//    @Override
//    //重写父类提供的跨域请求处理的接口
//    public void addCorsMappings(CorsRegistry registry) {
//        //添加映射路径
//        registry.addMapping("/**")
//                //是否发送Cookie信息
//                .allowCredentials(true)
//                //放行哪些原始域(请求方式)
//                .allowedMethods("GET","POST","OPTIONS")
//                //放行哪些原始域(头部信息)
//                .allowedHeaders("*")
//                //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
//                .exposedHeaders("X-token");
//    }

}
