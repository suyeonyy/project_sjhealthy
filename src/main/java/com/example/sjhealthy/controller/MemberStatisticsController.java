package com.example.sjhealthy.controller;

import com.example.sjhealthy.component.MemberStatisticsMapper;
import com.example.sjhealthy.dto.MemberStatisticsDTO;
import com.example.sjhealthy.dto.Response;
import com.example.sjhealthy.service.MemberStatisticsService;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            if (achievement == null){
                System.out.println("데이터가 존재하지 않습니다.");
                model.addAttribute("statistics", null);
            } else {
                System.out.println("통계 가져오기 성공");
                System.out.println(achievement);
                Double memberWeight = service.getMemberWeight(loginId); // 몸무게
                System.out.println("몸무게 가져오기 성공");
                System.out.println(memberWeight);

                model.addAttribute("statistics", achievement);
                model.addAttribute("memberWeight", memberWeight);
            }
        }

        List<MemberStatisticsDTO> list = service.getRankList();
        System.out.println("통계 리스트 받아옴");
        model.addAttribute("list", list);

        return "statistics/statMain";
    }

    @GetMapping("/statistics/graph")
    public String openWeightGraph(@SessionAttribute(name = "loginId", required = false) String loginId,
                                  Model model){
        model.addAttribute("loginId", loginId);
        return "statistics/weightChangeGraph";
    }

    @ResponseBody
    @PostMapping("/statistics/graph")
    public ResponseEntity<Response<Object>> createWeightChangeGraph(
        @SessionAttribute(name = "loginId", required = false) String loginId,
        Model model, @RequestBody Map<String, Integer> data){
        model.addAttribute("loginId", loginId);
        System.out.println("그래프 요청");

        int month = data.get("month");
        System.out.println("month: " + month);
        try {
            List<Map<String, Double>> weightList = service.getWeightListByMemberIdAndMonth(loginId, month);
            System.out.println("weightList = " + weightList);
            if (weightList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null, "데이터가 존재하지 않습니다."));
            }
            return ResponseEntity.ok(new Response<>(weightList, null));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "시스템 오류"));
        }
    }
}
