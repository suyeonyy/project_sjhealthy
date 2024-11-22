package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.service.MailServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sjhealthy/api/mail")
public class MailServiceController { // 이메일 인증용. js로 요청과 이메일을 받아서 처리
    @Autowired
    MailServiceImpl mailService;

    @PostMapping("/confirm/json")
    public String mailConfirm(@RequestBody Map<String, Object> data,
                              HttpSession session) throws Exception {
        String email = (String)data.get("email");
        String memberId = (String)data.get("memberId");
        String code = mailService.sendMessage(email);

        session.setAttribute("mailCode", code);
        session.setAttribute("memberId", memberId);
        System.out.println(code);
        return code;
    }
}
