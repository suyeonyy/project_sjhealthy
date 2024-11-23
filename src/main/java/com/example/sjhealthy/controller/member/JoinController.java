package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        return "login";
    }

    /********** 중복 체크 ********************************/
    // 아이디 중복검사
    @PostMapping("/member/idCheck.do")
    @ResponseBody
    public int joinIdCheck(@RequestParam String memberId) {
        System.out.println("중복체크");
        System.out.println(memberId);

        return memberService.memberIdCheck(memberId);
    }
}
