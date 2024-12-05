package com.example.sjhealthy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // CORS 설정하여 허용된 도메인에서만 요청할 수 있도록 엑세스 제어.
    // 오류나서 만듦

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8081")
            .allowedMethods("GET", "POST")
            .allowedHeaders("Authorization", "Content-Type")
            .allowCredentials(true); // 쿠키와 자격증명 전송 허용
    }

}
