package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/sjhealthy/")
@Controller
public class LoginController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/member/login")
    public String showLoginPage(){
        System.out.println("loginForm");
        return "login";
    }

    @PostMapping("/member/login")
    public String loginFunction(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        System.out.println("login");

        // TODO: write나 다른 위치에서 온 경우엔 다시 그 위치로 보내는 로직
        try {
            MemberDTO loginResult = memberService.login(memberDTO);
            if (loginResult != null){
//                로그인 성공
                session.setAttribute("loginId", loginResult.getMemberId());
                System.out.println("로그인 성공");
                return "main";
            } else {
//                로그인 실패
                System.out.println("로그인 실패");
                return "login";
            }
        } catch (Exception e){
            System.out.println("시스템 오류");
            return "main";
        }
    }
}

