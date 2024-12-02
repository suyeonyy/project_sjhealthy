package com.example.sjhealthy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SignInController {
    private final SignInService signInService;

    @GetMapping("kakao")
    public String signinKakao(@RequestParam("code") String code) {
        String accessToken = signInService.getKakaoAccessToken(code);
        String userInfo = signInService.getUserInfo(accessToken);
        return "redirect:/home";
    }
}
