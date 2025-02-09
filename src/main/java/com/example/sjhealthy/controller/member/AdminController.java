package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.BoardDTO;
import org.springframework.hateoas.EntityModel;
import com.example.sjhealthy.component.MemberMapper;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.dto.Response;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.service.AdminService;
import com.example.sjhealthy.service.BoardService;
import com.example.sjhealthy.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/sjhealthy/")
@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardService boardService;

    @GetMapping("/admin")
    public String getAdminForm(@SessionAttribute(name = "loginId", required = false)String loginId, Model model,
                               RedirectAttributes ra){
        model.addAttribute("loginId", loginId);

        if (loginId != null) {
            MemberDTO loginMember = memberService.findMemberIdAtPassFind(loginId);

            if (loginMember.getMemberAuth().equals("A") || loginMember.getMemberId().equals("admin")) {
                List<MemberDTO> dtoList = memberService.getMemberList();
                model.addAttribute("memberList", dtoList);
                return "adminPage";
            }
        }
        ra.addAttribute("message", "관리자만 접근 가능한 페이지입니다.");
        return "redirect:/sjhealthy";
    }

    @ResponseBody
    @GetMapping("/admin/member")
    public ResponseEntity<Response<Object>> getMemberList(Model model, @RequestParam(name="page", defaultValue = "1") int page,
                                                          PagedResourcesAssembler<MemberDTO> assembler,
                                                          @SessionAttribute(name = "loginId", required = false)String loginId) {
        model.addAttribute("loginId", loginId);

        int pageSize = 10;

        try {
            if (loginId != null) {
                MemberDTO loginMember = memberService.findMemberIdAtPassFind(loginId);

                if (loginMember.getMemberAuth().equals("A")) {
                    // 관리자인지 확인
                    Page<MemberDTO> dtoList = memberService.getMemberListWithPage(page, pageSize);
                    if (dtoList == null || dtoList.isEmpty()){
                        // 게시물 없을 때
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null, "게시물이 없습니다."));
                    } else {
                        PagedModel<EntityModel<MemberDTO>> memberList = assembler.toModel(dtoList);
                        return ResponseEntity.ok().body(new Response<>(memberList, "리스트를 가져왔습니다."));
                    }
                } else { // HTTP 코드 403 반환
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response<>(null,
                        "관리자만 접근 가능한 페이지입니다."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response<>(null,
                    "관리자만 접근 가능한 페이지입니다."));
            }
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null,
                "시스템 오류"));
        }
    }

    @ResponseBody
    @GetMapping("/admin/member/delete/{memberId}")
    public ResponseEntity<Response<Object>> getMemberEntityForDelete(@PathVariable String memberId){
        if (memberId == null){
            // 제대로 못 받았을 때
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null, "아이디를 불러오지 못하였습니다."));
        }

        memberService.delete(memberId);
        System.out.println("회원 탈퇴 = " + memberId);
        return ResponseEntity.ok().body(new Response<>(null, "탈퇴를 완료하였습니다."));
    }

    @ResponseBody // 페이지 추가
    @GetMapping("/admin/post")
    public ResponseEntity<PagedModel<EntityModel<BoardDTO>>> getAllPost(@RequestParam(defaultValue = "1", name = "page") int page, Model model,
                                                                        @SessionAttribute(name = "loginId", required = false)String loginId,
                                                                        PagedResourcesAssembler<BoardDTO> assembler) {
        model.addAttribute("loginId", loginId);

        int pageSize = 10;

        if (loginId != null) {
            MemberDTO loginMember = memberService.findMemberIdAtPassFind(loginId);
            System.out.println(loginMember);

            if (loginMember.getMemberAuth().equals("A")) {
                // 관리자인지 확인
                Page<BoardDTO> board = boardService.getListWithPage(page, pageSize);
                System.out.println(board);
                PagedModel<EntityModel<BoardDTO>> boardList = assembler.toModel(board);

                return ResponseEntity.ok().body(boardList);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
