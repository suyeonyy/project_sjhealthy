package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/sjhealthy/")

public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")
    public String WriteForm(@SessionAttribute(name = "loginId", required = false) String loginId,
                            Model model){
        model.addAttribute("loginId", loginId);
        // 회원인지 확인
        if (loginId == null){
            // 회원이 아니라면 로그인 창으로 연결
            return "login";
        } else {
            // 회원은 글 작성 뷰로 연결
            model.addAttribute("loginId", loginId);
            return "board/write";
        }
    }

    @PostMapping("/board/write")
    public String WriteNewPost(@SessionAttribute(name = "loginId", required = false) String loginId,
                               @ModelAttribute BoardDTO boardDTO, RedirectAttributes ra, Model model){
        model.addAttribute("loginId", loginId);

        try {
            BoardDTO writeResult = boardService.write(boardDTO);

            if (writeResult != null) {
                ra.addAttribute("boardId", boardDTO.getBoardId());
                ra.addFlashAttribute("boardDTO", boardDTO);
                System.out.println("글 등록 성공");
                return "redirect:/sjhealthy/board/read";
            } else {
                System.out.println("글 등록 실패");
                return "main";
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("시스템 오류로 실패");
            return "main";
        }
    }

    @GetMapping("/board/read")
    public String readPost(@ModelAttribute(name = "boardDTO")BoardDTO dto, Model model,
                           @SessionAttribute(name = "loginId", required = false) String loginId,
                           RedirectAttributes ra){
        // TODO: dto 비어있을 때 게시글을 읽어오지 못하였습니다
        // TODO: 작성일이 출력이 안 됨..
        // TODO: Redirect로 보낸 거 일회용이라 새로고침 하면 내용 사라짐

        model.addAttribute("boardDTO", dto);
        model.addAttribute("loginId", loginId);

        return "board/read";
    }
}
