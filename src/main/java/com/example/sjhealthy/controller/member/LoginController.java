package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/member/find-id")
    public String findIdForm(){
        System.out.println("find-id Form");

        return "findId";
    }

    @PostMapping("/member/find-id")
    public String findIdAfterPost(@ModelAttribute MemberDTO memberDTO, Model model){ // 이름, 생년월일로 찾기
        try {
            MemberDTO findIdResult = memberService.findMemberId(memberDTO);

            if (findIdResult != null){
                System.out.println("아이디: " + memberDTO.getMemberId());
                model.addAttribute("memberDTO", findIdResult);
            }
            return "findId"; // 양식만 바꿔 같은 뷰 사용

        } catch (Exception e){
            System.out.println("시스템 오류");
            return "redirect:/sjhealthy/member/login";
        }
    }

    @GetMapping("/member/find-password")
    public String findPasswordForm(){
        System.out.println("find-password Form");

        return "findPassword";
    }

    @PostMapping("/member/find-password")
    public String findPasswordAfterPost(@ModelAttribute MemberDTO memberDTO, Model model){
        // 컨트롤러에서 아이디 존재하는지 확인 후 메일 인증 진행
        // 메일로 인증메일 발송
        try {
            MemberDTO byMemberId = memberService.findMemberId(memberDTO);

            if (byMemberId != null){
                return null; // 아직
            }
        } catch (Exception e){
            System.out.println("시스템 오류");
            return "redirect:/sjhealthy/member/login";
        }
        return  null;
    }
}

