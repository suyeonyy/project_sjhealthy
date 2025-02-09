package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.DailyEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.service.DailyService;
import com.example.sjhealthy.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.source.tree.MemberReferenceTree;
import jakarta.persistence.Tuple;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sjhealthy/")
public class DailyController {
    private final DailyService dailyService;
    private final MemberService memberService;

    @GetMapping("/daily/dailyList")
    public String getDailyList(@SessionAttribute(name = "loginId", required = false) String loginId, Model model) {

        model.addAttribute("loginId", loginId);

        //현재 날짜 가져오기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());
        String year = strToday.substring(0,4);
        String month = strToday.substring(4,6);

        List<DailyDTO> dailyList = dailyService.getDateList(loginId, year, month);
        model.addAttribute("dailyList", dailyList);

        //로그인 했을 때
        if(loginId != null){
            MemberDTO dto = memberService.findMemberIdAtPassFind(loginId);
            if(dto.getMemberAuth().equals("A")){ //관리자
                model.addAttribute("administrator", dto.getMemberAuth());
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

                //ra.addAttribute("loginId", loginId); //addAttribute로 보내면 리다이렉트 값 유지못함
                ra.addFlashAttribute("loginId", loginId);
                ra.addFlashAttribute("alertMessage", "일지가 작성되었습니다.");


                //'../daily/dailyRead?dailyDate=' + encodeURIComponent(dailyDate);
                Map<String, String> response = new HashMap<>();
                //String redirectUrl = "/sjhealthy/daily/dailyList";
                String redirectUrl = "/sjhealthy/daily/dailyRead?dailyDate=" + writeResult.getDailyDate();
                String alertMessage = "일지가 작성되었습니다."; // 알림 메시지
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
                           @RequestParam("dailyDate") String dailyDate, Model model,
                           HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("loginId", loginId);

        MemberEntity memberEntity = memberService.findMemberEntity(loginId);

        try{
            DailyDTO result = dailyService.read(loginId, dailyDate);

            if(loginId != null){
            }

            model.addAttribute("dailyDTO", result);
            return "daily/dailyRead";
        }catch (Exception e){
            System.out.println("시스템 오류로 글을 읽어오지 못했습니다.");
            e.printStackTrace();
            return "redirect:/sjhealthy/daily/dailyList";
        }
    }

    @GetMapping("/daily/daygrid-views")
    public String getDayGridViews(@SessionAttribute(name = "loginId", required = false) String loginId,
                                Model model){
        model.addAttribute("loginId", loginId);

        return "daily/daygrid-views";
    }

    @GetMapping("/daily/update")
    public String getUpdateForm(@RequestParam("dailyId") Long dailyId, Model model,
                                @SessionAttribute(name = "loginId", required = false) String loginId){
        model.addAttribute("loginId", loginId);

        if (loginId == null){
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        } else {
            // 회원은 글 수정 뷰로 연결
            DailyDTO dailyDTO = dailyService.readView(dailyId);

            model.addAttribute("dailyDTO", dailyDTO);
            model.addAttribute("loginId", loginId);
            return "daily/dailyUpdate";
        }
    }

    @PostMapping("/daily/update")
    public ResponseEntity<Map<String, String>> updatePost(@ModelAttribute DailyDTO dailyDTO, Model model, RedirectAttributes ra,
                             @SessionAttribute(name = "loginId", required = false) String loginId,
                             @RequestParam("dailyId") Long dailyId){
        model.addAttribute("loginId", loginId);

        try {
            DailyDTO postDTO = dailyService.readView(dailyId);
            postDTO.setDailyTitle(dailyDTO.getDailyTitle());
            postDTO.setDailyCurWt(dailyDTO.getDailyCurWt());
            postDTO.setDailyGoalWt(dailyDTO.getDailyGoalWt());
            postDTO.setDailyGoalSf(dailyDTO.getDailyGoalSf());
            postDTO.setDailyMemo(dailyDTO.getDailyMemo());
            postDTO.setDailyYear(dailyDTO.getDailyYear());
            postDTO.setDailyMonth(dailyDTO.getDailyMonth());

            DailyDTO updateResult = dailyService.update(postDTO);

            if (updateResult != null) {
                /*
                ra.addAttribute("dailyId", dailyDTO.getDailyId());
                ra.addAttribute("message", "일지 수정이 완료되었습니다.");//alertMessage
                ra.addFlashAttribute("dailyDTO", dailyDTO);
                System.out.println("일지 수정 성공");
                return "redirect:/sjhealthy/daily/dailyRead";
                */
                ra.addFlashAttribute("dailyDTO", updateResult);
                ra.addFlashAttribute("loginId", loginId);
                ra.addFlashAttribute("alertMessage", "일지가 수정되었습니다.");
                Map<String, String> response = new HashMap<>();
                String redirectUrl = "/sjhealthy/daily/dailyRead?dailyDate=" + updateResult.getDailyDate();
                String alertMessage = "일지가 수정되었습니다."; // 알림 메시지
                response.put("redirectUrl", redirectUrl);
                response.put("alertMessage", alertMessage);
                // 리다이렉트 URL을 JSON 형태로 응답
                return ResponseEntity.ok(response);

            } else {
                /*
                ra.addAttribute("dailyId", dailyDTO.getDailyId());
                ra.addAttribute("message", "일지 수정에 실패했습니다.");
                System.out.println("일지 수정 실패");
                return "redirect:/sjhealthy/daily/dailyRead";
                */
                Map<String, String> response = new HashMap<>();
                String redirectUrl = "/sjhealthy/daily/dailyRead";
                response.put("redirectUrl", redirectUrl);

                System.out.println("일지 수정 실패");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e){
            /*
            e.printStackTrace(); // 오류 떠서 이유 확인용
            System.out.println("시스템 오류로 실패");
            ra.addAttribute("message", "시스템 오류로 글 수정에 실패했습니다.");
            return "redirect:/sjhealthy";
            */
            Map<String, String> response = new HashMap<>();
            String redirectUrl = "/sjhealthy/daily/dailyRead";
            response.put("redirectUrl", redirectUrl);

            System.out.println("일지 수정 실패");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/daily/dailyNewList")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDailyNewList (@SessionAttribute(name = "loginId", required = false) String loginId,
                                                                @RequestParam("year") String year, @RequestParam("month") String month, Model model) {
        Map<String, Object> response = new HashMap<>();

        model.addAttribute("loginId", loginId);

        List<DailyDTO> dailyList = dailyService.getDateList(loginId, year, month);

        //로그인 했을 때
        if(loginId != null){
            if (dailyList != null) {
                response.put("dailyList", dailyList);
            }
        } else {
            response.put("dailyList", null);
        }

        return ResponseEntity.ok(response);  // JSON 형태로 응답 반환
    }
}
