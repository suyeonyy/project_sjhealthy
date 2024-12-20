package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.CommentDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.repository.BoardRepository;
import com.example.sjhealthy.service.BoardService;
import com.example.sjhealthy.service.CommentService;
import com.example.sjhealthy.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sjhealthy/")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/board/list")
    public String getBoardList(@SessionAttribute(name = "loginId", required = false) String loginId,
                               Model model){
        model.addAttribute("loginId", loginId);

        List<BoardDTO> boardList = boardService.getList();
        model.addAttribute("boardList", boardList);

        // 로그인 했을 때
        if (loginId != null){
            MemberDTO dto = memberService.findMemberIdAtPassFind(loginId);
            if (dto.getMemberAuth().equals("admin")){ // 관리자
                model.addAttribute("admin", dto);
            }
        }

        return "board/list";
    }

    @GetMapping("/board/write")
    public String writeForm(@SessionAttribute(name = "loginId", required = false) String loginId,
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
    public String writeNewPost(@SessionAttribute(name = "loginId", required = false) String loginId,
                               @ModelAttribute BoardDTO boardDTO, RedirectAttributes ra, Model model, MultipartFile file){
        model.addAttribute("loginId", loginId);

        try {
            if (!file.isEmpty()){ // 첨부파일이 존재한다면
                saveFile(file, boardDTO); // boardDTO에 MultipartFile로 읽어온 첨부파일 추가
                System.out.println(file);
            }
            BoardDTO writeResult = boardService.write(boardDTO);
            System.out.println(writeResult);

            if (writeResult != null) {
                ra.addAttribute("boardId", writeResult.getBoardId());
                ra.addFlashAttribute("boardDTO", writeResult);
                System.out.println("글 등록 성공");
                return "redirect:/sjhealthy/board/read";
            } else {
                System.out.println("글 등록 실패");
                return "redirect:/sjhealthy/board";
            }
        } catch (Exception e){
            e.printStackTrace(); // 오류 떠서 이유 확인용
            System.out.println("시스템 오류로 실패");
            return "redirect:/sjhealthy/board";
        }
    }

    public void saveFile(MultipartFile file, BoardDTO boardDTO) throws IOException {
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID();
        String fileOriginName = file.getOriginalFilename();
        String fileName = uuid + "_" + fileOriginName; // 저장용 이름 만들어줌

        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        boardDTO.setBoardFileName(fileName); // DB에 추가
        boardDTO.setBoardFilePath("/files/" + fileName);
    }

    @RequestMapping("/board/read")
    public String readPost(@RequestParam("boardId") Long boardId, Model model,
                           @SessionAttribute(name = "loginId", required = false) String loginId,
                           HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("loginId", loginId);

        try {
            BoardDTO result = boardService.read(boardId);
            if (result.getBoardFileName() != null && !result.getBoardFileName().isEmpty()) { // 첨부파일 있을 때 출력
                model.addAttribute("attachedFile", result.getBoardFilePath());
            }

            // 조회수 브라우저 종료시 다시 집계됨
            Cookie viewCount = addViews(request, boardId, response, result);
            if (viewCount != null){
                response.addCookie(viewCount);
            }

            if (loginId != null){ // 로그인 한 상태라면
                // 관리자
                MemberDTO dto = memberService.findMemberIdAtPassFind(loginId);

                if (dto.getMemberAuth().equals("A")){ // 관리자
                    model.addAttribute("admin", dto.getMemberAuth());
                }
                /*
                if (dto.getMemberAuth().equals("admin")){ // 관리자
                    model.addAttribute("admin", dto);
                }
                */
            }

            //댓글 목록 가져오기
            List<CommentDTO> commentDTOList = commentService.findAll(boardId);
            model.addAttribute("commentList", commentDTOList);

            model.addAttribute("boardDTO", result);
            return "board/read";
        } catch (Exception e){
            System.out.println("시스템 오류로 글을 읽어오지 못했습니다.");
            e.printStackTrace();
            return "redirect:/sjhealthy/board/list";
        }
    }

    public Cookie addViews(HttpServletRequest request, Long boardId, HttpServletResponse response, BoardDTO boardDTO){
        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            boardService.countBoardView(boardDTO);
            // 쿠키가 없다면 생성
            Cookie newCookie = new Cookie("view_count", boardId.toString());
            newCookie.setPath("/");
            return newCookie;
        } else {
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("view_count")){
                    if (!cookie.getValue().contains(boardId.toString())){
                        // 조회 기록 없을시 조회수 1 증가
                        boardService.countBoardView(boardDTO);
                        cookie.setValue(cookie.getValue() + "_" + boardId);
                        return cookie;
                    } else return null;
                }
            }
        }
        // 쿠키는 있는데 view_count가 없을 때
        boardService.countBoardView(boardDTO);
        Cookie newCookie = new Cookie("view_count", boardId.toString());
        newCookie.setPath("/");
        return newCookie;
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
                             @SessionAttribute(name = "loginId", required = false) String loginId,
                             @RequestParam("boardId") Long boardId){
        model.addAttribute("loginId", loginId);

        try {
            BoardDTO postDTO = boardService.read(boardId);
            postDTO.setBoardTitle(boardDTO.getBoardTitle());
            postDTO.setBoardContent(boardDTO.getBoardContent());

            BoardDTO updateResult = boardService.write(postDTO);

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
            return "redirect:/sjhealthy";
        }
    }

    @RequestMapping("/board/delete")
    public String deletePost(@RequestParam("boardId") Long boardId, RedirectAttributes ra){
        boolean isDeleted = boardService.delete(boardId);
        if (isDeleted){
            System.out.println("글이 삭제되었습니다.");
            return "redirect:/sjhealthy/board/list";
        } else {
            System.out.println("글을 삭제하지 못했습니다.");
            ra.addAttribute("boardId", boardId);
            return "redirect:/sjhealthy/board/read";
        }
    }
}
