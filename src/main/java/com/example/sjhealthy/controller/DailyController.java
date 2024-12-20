package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.DailyService;
import com.example.sjhealthy.service.MemberService;
import com.sun.source.tree.MemberReferenceTree;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

        List<DailyDTO> dailyList = dailyService.getList();
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

    @GetMapping("/daily/dailyWrite")
    public String getDailyWrite(@SessionAttribute(name = "loginId", required = false) String loginId,
                               Model model){
        model.addAttribute("loginId", loginId);

        return "daily/dailyWrite";
    }

    @PostMapping("/daily/dailyWrite")
    public ResponseEntity<String> postDailyWrite(@SessionAttribute(name = "loginId", required = false) String loginId,
                                 @ModelAttribute DailyDTO dailyDTO, Model model, RedirectAttributes ra){
        model.addAttribute("loginId", loginId);

        System.out.println("진입하나용????");

        try {
            DailyDTO writeResult = dailyService.write(dailyDTO);

            if (writeResult != null) {
                ra.addFlashAttribute("dailyDTO", writeResult);
                System.out.println("일지 등록 성공");

                //ra.addAttribute("loginId", loginId); //addAttribute로 보내면 리다이렉트 값 유지못함
                ra.addFlashAttribute("loginId", loginId);

                // 리다이렉트 URL을 JSON 형태로 응답
                return ResponseEntity.ok("/sjhealthy/daily/dailyList");
                //return "redirect:/sjhealthy/daily/dailyList";
            } else {
                System.out.println("일지 등록 실패");
                return ResponseEntity.ok("/sjhealthy/daily/dailyList");
                //return "redirect:/sjhealthy/daily/dailyList";
            }

        } catch (Exception e){
            e.printStackTrace(); //오류 원인 확인용
            System.out.println("시스템 오류로 실패");
            return ResponseEntity.ok("/sjhealthy/daily/dailyList");
            //return "redirect:/sjhealthy/daily/dailyList";
        }
    }
}
