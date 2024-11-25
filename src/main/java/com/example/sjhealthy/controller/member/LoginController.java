package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.MailServiceImpl;
import com.example.sjhealthy.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/sjhealthy/")
@Controller
public class LoginController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MailServiceImpl mailService;

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
                return "redirect:/sjhealthy";
            } else {
//                로그인 실패
                System.out.println("로그인 실패");
                return "redirect:/sjhealthy/member/login";
            }
        } catch (Exception e){
            System.out.println("시스템 오류");
            return "redirect:/sjhealthy";
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

//    @GetMapping("/member/find-password")
//    인증메일을 입력한 아이디를 조회해서 해당 계정의 이메일인지 확인하는 과정 필요
    @PostMapping("/member/find-password")
    public String findPasswordAfterPost(@ModelAttribute MemberDTO memberDTO, Model model,
                                        @SessionAttribute(name = "mailCode", required = false) String mailCode,
                                        @SessionAttribute(name = "memberId", required = false) String memberId,
                                        @RequestParam(name = "verificationCode")String inputCode){
        // 컨트롤러에서 아이디 존재하는지 확인 후 메일 인증 진행
        // 메일로 인증메일 발송 (이 과정은 RestController와 js로)
        try {
            System.out.println(memberId + " " + mailCode);
            MemberDTO byMemberId = memberService.findMemberIdAtPassFind(memberId);
            System.out.println(byMemberId);

            if (byMemberId != null){
                if (mailCode.equals(inputCode)){
                    System.out.println("입력 코드: " + inputCode);
                    System.out.println("인증 코드: " + mailCode);
                    return "setNewPassword";
                } else {
                    System.out.println("코드가 일치하지 않습니다.");
                }
            } else {
                System.out.println("아이디가 존재하지 않습니다.");
            }
        } catch (Exception e){
            System.out.println("시스템 오류");
        }
        return "redirect:/sjhealthy/member/login";
    }


    @PostMapping("/member/set-new-password")
    public String setNewPassword(@RequestParam("password")String password, RedirectAttributes ra,
//                                 @RequestParam("passwordCheck")String passwordCheck,
                                 @SessionAttribute(name = "memberId", required = false) String memberId){
        System.out.println(password);

        try {
            MemberDTO beforeUpdate = memberService.findMemberIdAtPassFind(memberId);
            // 뷰에서 일치 검사
            if (!password.equals(beforeUpdate.getMemberPassword())){
                beforeUpdate.setMemberPassword(password);
                memberService.join(beforeUpdate); // 바꾼 비밀번호로 정보 수정(JPA 에선 save로 등록과 수정을 한다)
                ra.addAttribute("비밀번호를 변경하였습니다.");
                return "redirect:/sjhealthy/member/login";
            } else {
                // 기존 비밀번호와 동일하면 돌려보냄
                System.out.println("기존 비밀번호와 동일한 비밀번호입니다.");
                ra.addAttribute("기존 비밀번호와 동일한 비밀번호입니다.");
                return "redirect:/sjhealthy/member/login";
            }
        } catch (Exception e){
            System.out.println("시스템 오류");
            e.printStackTrace();
            return "redirect:/sjhealthy/member/login";
        }
    }

    @RequestMapping("/member/logout")
    public String logout(@SessionAttribute(name = "loginId", required = false) String loginId, Model model,
                         HttpSession session){
        model.addAttribute("loginId", loginId);

        if (loginId != null){
            session.invalidate(); // 세션 무효화
        }
        return "redirect:/sjhealthy";
    }
}

