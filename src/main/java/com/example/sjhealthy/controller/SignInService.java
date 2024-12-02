package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.KakaoApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final KakaoApiClient kakaoApiClient;

    private static final Logger log = LoggerFactory.getLogger(SignInService.class);

    public String getKakaoAccessToken(String code) {
        String requestUrl = kakaoApiClient.getAuthUrl() + "/oauth/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> httpBodies = new LinkedMultiValueMap<>();
        httpBodies.add("grant_type", "authorization_code");
        httpBodies.add("client_id", kakaoApiClient.getClientId());
        httpBodies.add("redirect_uri", kakaoApiClient.getRedirectUrl());
        httpBodies.add("code", code);
        httpBodies.add("client_secret", kakaoApiClient.getSecretKey());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(httpBodies, httpHeaders);
        KakaoToken response = restTemplate.postForObject(requestUrl, kakaoTokenRequest, KakaoToken.class);

        return response.getAccessToken();
    }

    public String getUserInfo(String accessToken) {
        String requestUrl = kakaoApiClient.getApiUrl() + "/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(httpHeaders);
        KakaoUserInfo response = restTemplate.postForObject(requestUrl, kakaoUserInfoRequest, KakaoUserInfo.class);

        log.info("id: {}", response.getId());
        log.info("email: {}", response.getEmail());
        log.info("nickname: {}", response.getNickname());
        return response.getEmail();
    }
}
