package com.example.sjhealthy.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class KakaoApiClient {
    /* 로그인에 필요한 키 설정 */
    
    // @Value 애너테이션을 사용하지 않으므로 별도의 값을 주입해주는 방식으로 처리
    private String clientId;

    private String secretKey;

    private String authUrl;	// https://kauth.kakao.com

    private String apiUrl; // https://kapi.kakao.com

    private String redirectUrl;	// http://localhost:8080/signin/kakao
}
