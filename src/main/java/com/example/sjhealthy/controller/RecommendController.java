package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/sjhealthy/")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @GetMapping({"/recommend", "/recommend/"})
    public String getRecommendList(@SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

        List<RecommendDTO> list = recommendService.getList();
        model.addAttribute("list", list);

        return "recommend/recList";
    }
    @GetMapping("/recommend/write")
    public String getRecommendForm(@SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

//        // 회원 전용 - 뷰에서 함.
//        if (loginId == null){
//            return "redirect:/sjhealthy/recommend";
//        }
        return "recommend/writeRec";
    }
}
