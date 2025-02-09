package com.example.sjhealthy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

// env.properties 파일을 사용하기 위해 Spring boot에 연결하는 설정 파일
// https://naturecancoding.tistory.com/96 참고
@Configuration
@PropertySources({
    @PropertySource("classpath:properties/key.properties") // key.properties 파일 소스 등록
})
public class PropertyConfig {
}
