package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.AdminService;
import com.example.sjhealthy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RequestMapping("/sjhealthy/")
@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/admin")
    public String getMemberList(Model model, @SessionAttribute(name = "loginId", required = false)String loginId) {
        model.addAttribute("loginId", loginId);
        if (loginId != null) {
            MemberDTO loginMember = memberService.findMemberIdAtPassFind(loginId);
            System.out.println(loginMember);

            if (loginMember.getMemberAuth().equals("A")) {
                // 관리자인지 확인
                List<MemberDTO> dto = memberService.getMemberList();
                model.addAttribute("memberList", dto);
                return "adminPage";
            } else {
                System.out.println("관리자만 접근 가능한 페이지입니다.");
                return "redirect:/sjhealthy";
            }
        } else {
            System.out.println("관리자만 접근 가능한 페이지입니다.");
            return "redirect:/sjhealthy/member/login";
        }
    }
}
