package com.example.sjhealthy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sjhealthy/")
public class infoController {
    @GetMapping("/info/infoList")
    public String getInfoList(){
        return "info/infoList";
    }
}
