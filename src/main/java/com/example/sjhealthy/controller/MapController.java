package com.example.sjhealthy.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/sjhealthy/")
@Controller
@RequiredArgsConstructor
public class MapController {
    //아래 final + RequiredArgsConstructor 적으면 autowird 안적어도됌


    @GetMapping("/map")
    public String getMapForm(){
        return "map";
    }
}
