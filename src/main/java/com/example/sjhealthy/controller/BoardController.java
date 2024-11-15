package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/sjhealthy/")

public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/save")
    public String WriteForm(@SessionAttribute(name = "loginId", required = false) String loginId){
        // 회원인지 확인
        if (loginId == null){
            // 회원이 아니라면 로그인 창으로 연결
            return "login";
        } else {
            // 회원은 글 작성 뷰로 연결
            return "save";
        }
    }

    @PostMapping("/board/save")
    public String WriteNewPost(@ModelAttribute BoardDTO boardDTO, Model model){
        try {
            BoardDTO writeResult = boardService.write(boardDTO);

            if (writeResult != null) {
                model.addAttribute("board", boardDTO);
                System.out.println("글 등록 성공");
                return "main";
            } else {
                System.out.println("글 등록 실패");
                return "main";
            }
        } catch (Exception e){
            System.out.println("시스템 오류로 실패");
            return "main";
        }
    }
}
