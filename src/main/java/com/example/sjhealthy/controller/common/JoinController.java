package com.example.sjhealthy.controller.common;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/sjhealthy/")
@Controller
public class JoinController {
    //생성자 주입
    //JoinController 클래스에 대한 객체를 스프링빈으로 등록할 때, 자동적으로 memberService에 대한 객체를 주입받는다
    //(Controller 클래스가 Service 클래스의 자원을 사용할 수 있는 권한이 생긴다)
    private MemberService memberService;

    //회원가입 페이지 출력 요청
    @GetMapping("/member/join")
    public String joinForm(){
        System.out.println("joinForm");
        return "join";
    }

    @PostMapping("/member/join")
    public String join(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        System.out.println("회원가입@@@@@@@@@@@");

        MemberDTO joinResult = memberService.join(memberDTO);
        
        
        return "login";
    }
}
