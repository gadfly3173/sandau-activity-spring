package vip.gadfly.sandauactivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SandauActivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandauActivityApplication.class, args);
    }

    @Bean(name="RestTemplate")
    @Autowired
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder){
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate;
    }
}
