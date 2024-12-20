package com.example.sjhealthy.controller;

import com.example.sjhealthy.component.MemberStatisticsMapper;
import com.example.sjhealthy.dto.MemberStatisticsDTO;
import com.example.sjhealthy.service.MemberStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/sjhealthy/")
public class MemberStatisticsController {
    @Autowired
    private MemberStatisticsService service;

//    TODO: 회원가입 후 회원 키, 몸무게 입력 칸 추가
    @GetMapping({"/statistics", "/statistics/"})
    public String getStatistics(@SessionAttribute(name = "loginId", required = false) String loginId,
                                     Model model){
        model.addAttribute("loginId", loginId);
        System.out.println("openStatisticsForm");
        if (loginId != null){ // 로그인 했을 땐 memberId, 본인 순위, 목표달성도, BMI 출력
            System.out.println("로그인 멤버");
            MemberStatisticsDTO achievement = service.getStatisticsByMemberId(loginId);
            System.out.println("통계 가져오기 성공");
            System.out.println(achievement);
            Double memberWeight = service.getMemberWeight(loginId); // 몸무게
            System.out.println("몸무게 가져오기 성공");
            System.out.println(memberWeight);

            model.addAttribute("statistics", achievement);
            model.addAttribute("memberWeight", memberWeight);
        }

        List<MemberStatisticsDTO> list = service.getRankList();
        System.out.println("통계 리스트 받아옴");
        model.addAttribute("list", list);
        System.out.println("통계 " + list);


        return "statistics/statMain";
    }
}
