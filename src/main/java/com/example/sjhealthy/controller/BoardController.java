package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.repository.BoardRepository;
import com.example.sjhealthy.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sjhealthy/")

public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/list")
    public String getBoardList(@SessionAttribute(name = "loginId", required = false) String loginId,
                               Model model){
        model.addAttribute("loginId", loginId);

        List<BoardDTO> boardList = boardService.getList();
        model.addAttribute("boardList", boardList);

        return "board/list";
    }

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
            e.printStackTrace(); // 오류 떠서 이유 확인용
            System.out.println("시스템 오류로 실패");
            return "main";
        }
    }

    @RequestMapping("/board/read")
    public String readPost(@RequestParam("boardId") Long boardId, Model model,
                           @SessionAttribute(name = "loginId", required = false) String loginId,
                           RedirectAttributes ra){
        model.addAttribute("loginId", loginId);

        // TODO: dto 비어있을 때 게시글을 읽어오지 못하였습니다
        // TODO: 작성일이 출력이 안 됨..
        // TODO: Redirect로 보낸 거 일회용이라 새로고침 하면 내용 사라짐

        try {
            BoardDTO result = boardService.read(boardId);
            model.addAttribute("boardDTO", result);
            return "board/read";
        } catch (Exception e){
            System.out.println("시스템 오류로 글을 읽어오지 못했습니다.");
            return "redirect:/sjhealthy/board/list";
        }
    }

    @GetMapping("/board/update")
    public String getUpdateForm(@RequestParam("boardId") Long boardId, Model model,
                             @SessionAttribute(name = "loginId", required = false) String loginId){
        model.addAttribute("loginId", loginId);

        if (loginId == null){
            return "board/login";
        } else {
            // 회원은 글 수정 뷰로 연결
            BoardDTO dto = boardService.read(boardId);
            model.addAttribute("loginId", loginId);
            model.addAttribute("isUpdate", dto);
            return "board/write"; // 뷰에서 확인하여 다른 폼 출력
        }
    }

    @PostMapping("/board/update")
    public String updatePost(@ModelAttribute BoardDTO boardDTO, Model model, RedirectAttributes ra,
                             @SessionAttribute(name = "loginId", required = false) String loginId){
        model.addAttribute("loginId", loginId);

        try {
            BoardDTO updateResult = boardService.write(boardDTO);

            if (updateResult != null) {
                ra.addAttribute("boardId", boardDTO.getBoardId());
                ra.addFlashAttribute("boardDTO", boardDTO);
                System.out.println("글 수정 성공");
                return "redirect:/sjhealthy/board/read";
            } else {
                ra.addAttribute("boardId", boardDTO.getBoardId());
                System.out.println("글 수정 실패");
                return "redirect:/sjhealthy/board/read";
            }
        } catch (Exception e){
            e.printStackTrace(); // 오류 떠서 이유 확인용
            System.out.println("시스템 오류로 실패");
            return "redirect:/sjhealthy/main";
        }
    }

    @RequestMapping("/board/delete")
    public String deletePost(){

        return "redirect:/sjhealthy/board/list";
    }
}
