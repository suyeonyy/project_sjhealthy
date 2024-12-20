package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String join(@ModelAttribute MemberDTO memberDTO, RedirectAttributes redirectAttributes){
        System.out.println("회원가입");
        System.out.println(memberDTO);
        memberService.join(memberDTO);

        // 리다이렉트 시 전달할 메시지
        redirectAttributes.addFlashAttribute("alertMessage", "회원가입이 완료되었습니다.\n로그인 후 이용해주세요.");

        return "redirect:/sjhealthy/member/login";
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
