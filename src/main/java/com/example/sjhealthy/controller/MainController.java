package com.example.sjhealthy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class MainController {

    @RequestMapping("/sjhealthy")
    public String index(@SessionAttribute(name = "loginId", required = false) String loginId, Model model) {
        System.out.println("index");
        model.addAttribute("loginId", loginId);

        return "main";
    }
}
