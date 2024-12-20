package com.example.sjhealthy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
        CORS(Cross-origin resource sharing): 교차 출처 리소스 공유
        CSRF(Cross-site request forgery): 사이트 간 요청 위조
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .csrf(AbstractHttpConfigurer::disable)		//csrf 방지
            .cors(AbstractHttpConfigurer::disable)      // cors 방지
            .formLogin(AbstractHttpConfigurer::disable) //기본 로그인페이지 없애기
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/member/login?logout"));
        return http.build();
    }

}
