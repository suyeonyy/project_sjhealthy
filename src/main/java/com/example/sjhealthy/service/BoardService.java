package com.example.sjhealthy.service;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RequiredArgsConstructor
@RequestMapping("/board/")
public class BoardService {
    private final BoardRepository boardRepository;

    @GetMapping("/save")
    public String WriteForm(@SessionAttribute(name = "loginMember", value = "false")MemberDTO loginDTO){
        // 회원인지 확인
        if (loginDTO == null){
            // 회원이 아니라면 로그인 창으로 연결
            return "login";
        } else {
            // 회원은 글 작성 뷰로 연결
             return "write";
        }
    }

    @PostMapping("/save")
    public String WriteNewPost(@ModelAttribute BoardDTO boardDTO){
        try {
            boolean WriteResult = boardRepository.insertPost(boardDTO);

            if (!WriteResult){
                // 글 올리기 실패
                System.out.println("글 업로드 실패");
                return "main";
            } else {
                // 글 올리기 성공
                System.out.println("글 업로드 성공");
                return "list";
            }
        } catch (Exception e){
            System.out.println("시스템상 오류로 글 업로드 실패");
            return "main";
        }
    }
}
