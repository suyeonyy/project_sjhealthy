package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/sjhealthy/")
@Controller
public class JoinController {
    //생성자 주입
    @Autowired
    private MemberService memberService;

    //회원가입 페이지 출력 요청
    @GetMapping("/member/join")
    public String joinForm(){
        return "join";
    }

    @PostMapping("/member/join")
    public String join(@ModelAttribute MemberDTO memberDTO){
        System.out.println("회원가입");
        System.out.println(memberDTO);
        memberService.join(memberDTO);

        return "join";
    }
}
