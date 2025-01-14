package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.service.DailyService;
import com.example.sjhealthy.service.MemberService;
import com.sun.source.tree.MemberReferenceTree;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sjhealthy/")
public class DailyController {
    private final DailyService dailyService;
    private final MemberService memberService;

    @GetMapping("/daily/dailyList")
    public String getDailyList(@SessionAttribute(name = "loginId", required = false) String loginId,
                               Model model){
        model.addAttribute("loginId", loginId);

        //현재 날짜 가져오기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());
        String year = strToday.substring(0,4);
        String month = strToday.substring(4,6);

        //List<DailyDTO> dailyList = dailyService.getList(loginId);
        List<String> dailyList = dailyService.getDateList(loginId, year, month);
        System.out.println("dailyList = " + dailyList);
        model.addAttribute("dailyList", dailyList);

        //로그인 했을 때
        if(loginId != null){
            MemberDTO dto = memberService.findMemberIdAtPassFind(loginId);
            if(dto.getMemberAuth().equals("A")){ //관리자
                model.addAttribute("admin", dto.getMemberAuth());
            }
        }

        return "daily/dailyList";
    }

    /*
    @GetMapping("/daily/dailyWrite")
    public String getDailyWrite(@SessionAttribute(name = "loginId", required = false) String loginId,
                               Model model){
        model.addAttribute("loginId", loginId);

        return "daily/dailyWrite";
    }
    */

    @GetMapping("/daily/dailyWriteDetail")
    public String getDailyWriteDetail(@SessionAttribute(name = "loginId", required = false) String loginId,
                                Model model){
        model.addAttribute("loginId", loginId);

        return "daily/dailyWriteDetail";
    }

    @PostMapping("/daily/dailyWriteDetail")
    public ResponseEntity<Map<String, String>> postDailyWrite(@SessionAttribute(name = "loginId", required = false) String loginId,
                                                              @ModelAttribute DailyDTO dailyDTO, Model model, RedirectAttributes ra){
        model.addAttribute("loginId", loginId);

        MemberEntity memberEntity = memberService.findMemberEntity(loginId);

        try {
            DailyDTO writeResult = dailyService.write(dailyDTO);

            if (writeResult != null) {
                ra.addFlashAttribute("dailyDTO", writeResult);
                System.out.println("일지 등록 성공");

                //ra.addAttribute("loginId", loginId); //addAttribute로 보내면 리다이렉트 값 유지못함
                ra.addFlashAttribute("loginId", loginId);
                ra.addFlashAttribute("alertMessage", "일지가 등록되었습니다.");

                Map<String, String> response = new HashMap<>();
                String redirectUrl = "/sjhealthy/daily/dailyList";
                String alertMessage = "일지가 등록되었습니다."; // 알림 메시지
                response.put("redirectUrl", redirectUrl);
                response.put("alertMessage", alertMessage);

                // 리다이렉트 URL을 JSON 형태로 응답
                return ResponseEntity.ok(response);
                //return ResponseEntity.ok("/sjhealthy/daily/dailyList");
                //return "redirect:/sjhealthy/daily/dailyList";
            } else {
                Map<String, String> response = new HashMap<>();
                String redirectUrl = "/sjhealthy/daily/dailyList";
                response.put("redirectUrl", redirectUrl);

                System.out.println("일지 등록 실패");
                return ResponseEntity.ok(response);
                //return ResponseEntity.ok("/sjhealthy/daily/dailyList");
                //return "redirect:/sjhealthy/daily/dailyList";
            }

        } catch (Exception e){
            Map<String, String> response = new HashMap<>();
            String redirectUrl = "/sjhealthy/daily/dailyList";
            response.put("redirectUrl", redirectUrl);

            e.printStackTrace(); //오류 원인 확인용
            System.out.println("시스템 오류로 실패");
            return ResponseEntity.ok(response);
            //return ResponseEntity.ok("/sjhealthy/daily/dailyList");
            //return "redirect:/sjhealthy/daily/dailyList";
        }
    }

    @RequestMapping("/daily/dailyRead")
    public String readPost(@SessionAttribute(name="loginId", required = false) String loginId,
                           @RequestParam("dailyId") Long dailyId, Model model,
                           HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("loginId", loginId);

        System.out.println("진입하나요??");

        MemberEntity memberEntity = memberService.findMemberEntity(loginId);

        try{
            // 여기 오류난다 수연아 잠깐 주석처리 해둘게
//            DailyDTO result = dailyService.read(dailyId);

            if(loginId != null){
            }

//            model.addAttribute("dailyDTO", result);
            return "daily/dailyRead";
        }catch (Exception e){
            System.out.println("시스템 오류로 글을 읽어오지 못했습니다.");
            e.printStackTrace();
            return "redirect:/sjhealthy/board/list";
        }
    }

    @GetMapping("/daily/daygrid-views")
    public String getDayGridViews(@SessionAttribute(name = "loginId", required = false) String loginId,
                                Model model){
        model.addAttribute("loginId", loginId);

        return "daily/daygrid-views";
    }

}
