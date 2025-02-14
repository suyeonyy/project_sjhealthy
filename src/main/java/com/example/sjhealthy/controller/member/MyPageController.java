package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.dto.Response;
import com.example.sjhealthy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.Map;

@Controller
@RequestMapping("/sjhealthy/")
public class MyPageController {

    @Autowired
    private MemberService memberService;

    // 회원 탈퇴는 LoginController에 있다.
    @GetMapping("/mypage")
    public String openMyPageForm(@SessionAttribute(name = "loginId", required = false) String loginId, Model model){
        model.addAttribute("loginId", loginId);

        if (loginId != null){
            // 회원
            MemberDTO member = memberService.findMemberIdAtPassFind(loginId);

            String memberBirth = member.getMemberBirth();
            String year = memberBirth.substring(0, 4);
            String month = memberBirth.substring(4, 6);
            String day = memberBirth.substring(6, 8);
            String sendDate = year + "/" + month + "/" + day;

            model.addAttribute("memberDTO", member);
            model.addAttribute("sendDate", sendDate);
        }
        return "mypage";
    }

    @ResponseBody
    @PostMapping("/mypage")
    public ResponseEntity<Void> checkPassword(@RequestBody Map<String, String> data){
        // 특별한 내용 없이 상태코드만 반환할 땐 Void / ?는 유연하게 / String은 메시지
        String memberId = data.get("memberId");
        String memberPassword = data.get("password");

        if (memberId == null || memberPassword == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            MemberDTO dto = new MemberDTO();
            dto.setMemberId(memberId);
            dto.setMemberPassword(memberPassword);

            // 로그인 메서드로 비밀번호 일치여부 확인
            MemberDTO result = memberService.login(dto);

            if (result != null){
                return ResponseEntity.ok().build();
            } else {
                // 비밀번호 불일치이므로 권한 없음(401)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ResponseBody
    @GetMapping("/mypage/division/{memberId}")
    public ResponseEntity<Response<Object>> checkMemberDivision(@PathVariable("memberId") String memberId){
        if (memberId == null || memberId.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "아이디가 정상적으로 전송되지 않았습니다."));
        }

        MemberDTO member = memberService.findMemberIdAtPassFind(memberId);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(null, "존재하지 않는 아이디입니다."));
        }

        return ResponseEntity.ok().body(new Response<>(member, "데이터 조회에 성공했습니다."));
    }
}
