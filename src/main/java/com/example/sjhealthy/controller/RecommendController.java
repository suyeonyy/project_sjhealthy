package com.example.sjhealthy.controller;

import com.example.sjhealthy.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RecommendController {
    @Autowired
    private RecommendService recommendService;


}
