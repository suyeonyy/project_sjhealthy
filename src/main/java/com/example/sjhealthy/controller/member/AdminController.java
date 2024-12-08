package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.AdminService;
import com.example.sjhealthy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private MemberService memberService;

    public String getMemberList(Model model){
        List<MemberDTO> dto = memberService.getMemberList();

        model.addAttribute("memberList", dto);
        return "adminPage";
    }


}
