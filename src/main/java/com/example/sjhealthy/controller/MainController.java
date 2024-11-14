package com.example.sjhealthy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping("/sjhealthy")
    @ResponseBody
    public String index() {
        System.out.println("index");
        return "index2";
    }
}
