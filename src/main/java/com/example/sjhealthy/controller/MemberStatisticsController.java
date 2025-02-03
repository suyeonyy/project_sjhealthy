package com.example.sjhealthy.controller;

import com.example.sjhealthy.component.MemberStatisticsMapper;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.dto.MemberStatisticsDTO;
import com.example.sjhealthy.dto.Response;
import com.example.sjhealthy.service.MemberService;
import com.example.sjhealthy.service.MemberStatisticsService;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sjhealthy/")
public class MemberStatisticsController {
    @Autowired
    private MemberStatisticsService service;

    @Autowired
    private MemberService memberService;

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
                Double memberWeight = service.getMemberWeight(loginId); // 몸무게
                Double memberHeight = memberService.findMemberIdAtPassFind(loginId).getMemberHeight();

                model.addAttribute("statistics", achievement);
                model.addAttribute("memberWeight", memberWeight);
                model.addAttribute("memberHeight", memberHeight);
            }
        }

        List<MemberStatisticsDTO> list = service.getRankList();
        if (list.size() > 3) {
            list = list.subList(0, 3); // 첫 5개만 선택
        }
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
        int year = data.get("year");
        System.out.println("year: " + year);

        try {
            List<Map<String, Double>> weightList = service.getWeightListByMemberIdAndMonth(loginId, month, year);
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

    @ResponseBody
    @GetMapping("/statistics/height/{height}")
    public ResponseEntity<Response<Object>> addMemberHeight(@PathVariable Double height, Model model,
                                                            @SessionAttribute(name = "loginId", required = false) String loginId){
        model.addAttribute("loginId", loginId);

        try {
            // 소수점 첫째 자리까지 사용
            Double roundedHeight = Math.floor(height * 10) / 10; //내림은 이용해서 첫째까지 남김
            MemberDTO dto = memberService.findMemberIdAtPassFind(loginId);
            memberService.addMemberHeight(roundedHeight, dto);

            return ResponseEntity.ok(new Response<>(dto, "키를 등록하였습니다."));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "시스템 오류로 실패하였습니다."));
        }
    }




    @PostMapping("/statistics/statMain")
    @ResponseBody  // 이 어노테이션을 추가하여 JSON 형식으로 응답을 반환
    public ResponseEntity<Map<String, Object>> getStatisticsData(@SessionAttribute(name = "loginId", required = false) String loginId) {
        Map<String, Object> response = new HashMap<>();

        response.put("loginId", loginId);

        if (loginId != null) {  // 로그인 했을 때
            System.out.println("로그인 멤버");
            MemberStatisticsDTO achievement = service.getStatisticsByMemberId(loginId);

            if (achievement == null) {
                System.out.println("데이터가 존재하지 않습니다.");
                response.put("statistics", null);
            } else {
                System.out.println("통계 가져오기 성공");
                Double memberWeight = service.getMemberWeight(loginId);  // 몸무게
                Double memberHeight = memberService.findMemberIdAtPassFind(loginId).getMemberHeight();

                response.put("statistics", achievement);
                response.put("memberWeight", memberWeight);
                response.put("memberHeight", memberHeight);
            }
        } else {
            response.put("statistics", null);
        }

        // 순위 리스트도 추가로 보내기
        List<MemberStatisticsDTO> list = service.getRankList();
        if (list.size() > 3) {
            list = list.subList(0, 3);  // 첫 3개만 선택
        }
        System.out.println("통계 리스트 받아옴");
        response.put("rankList", list);

        return ResponseEntity.ok(response);  // JSON 형태로 응답 반환
    }

}
