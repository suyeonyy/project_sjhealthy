package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.CommentDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.dto.Response;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.repository.BoardRepository;
import com.example.sjhealthy.service.BoardService;
import com.example.sjhealthy.service.CommentService;
import com.example.sjhealthy.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/board")
    public String openBoardForm(@SessionAttribute(name = "loginId", required = false) String loginId,
                               Model model, @RequestParam(name="page", defaultValue = "1") int page){
        model.addAttribute("loginId", loginId);
        model.addAttribute("page", page);

        return "board/list";
    }

    // 페이징 추가
    @ResponseBody
    @GetMapping("/board/list")
    public ResponseEntity<PagedModel<EntityModel<BoardDTO>>> getBoardList(
        @SessionAttribute(name = "loginId", required = false) String loginId, Model model,
        @RequestParam(name="page", defaultValue = "1") int page, PagedResourcesAssembler<BoardDTO> assembler) {

//        model.addAttribute("loginId", loginId); 이 형태에선 model도 못 쓴다
//        model.addAttribute("loginId", loginId); 이 형태에선 model도 못 쓴다
        int pageSize = 10;

        try {
            Page<BoardDTO> board = boardService.getListWithPage(page, pageSize);

            if (board == null || board.isEmpty()){
                // 게시물 없을 때
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                System.out.println(board);
                PagedModel<EntityModel<BoardDTO>> boardList = assembler.toModel(board);
                return ResponseEntity.ok().body(boardList);
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/board/current") // 메인페이지에 최근글 4개 출력
    public ResponseEntity<Response<Object>> getBoardListForMain(){
        List<BoardDTO> boardList = boardService.getListTop4();

        try {
            if (!boardList.isEmpty()){
                return ResponseEntity.ok(new Response<>(boardList, "게시물을 불러왔습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null, "게시물이 존재하지 않습니다."));
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "시스템 오류로 실패하였습니다."));
        }

    }
    @GetMapping("/board/write")
    public String writeForm(@SessionAttribute(name = "loginId", required = false) String loginId,
                            Model model, RedirectAttributes ra){
        model.addAttribute("loginId", loginId);
        // 회원인지 확인
        if (loginId == null){
            return "redirect:/sjhealthy/board";
        } else {
            // 회원은 글 작성 뷰로 연결
            model.addAttribute("loginId", loginId);
            return "board/write";
        }
    }

    @PostMapping("/board/write")
    public String writeNewPost(@SessionAttribute(name = "loginId", required = false) String loginId,
                               @ModelAttribute BoardDTO boardDTO, RedirectAttributes ra, Model model,
                               @RequestParam("file") MultipartFile file){
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
                //ra.addAttribute("message", "글이 작성되었습니다.");
                ra.addFlashAttribute("message", "글이 작성되었습니다.");
                ra.addFlashAttribute("boardDTO", writeResult);
                return "redirect:/sjhealthy/board/read";
            } else {
                //ra.addAttribute("message", "글 작성에 실패했습니다.");
                ra.addFlashAttribute("message", "글 작성에 실패했습니다.");
                return "redirect:/sjhealthy/board";
            }
        } catch (Exception e){
            e.printStackTrace(); // 오류 떠서 이유 확인용
            //ra.addAttribute("message", "시스템 오류로 글 작성에 실패했습니다.");
            ra.addFlashAttribute("message", "시스템 오류로 글 작성에 실패했습니다.");
            return "redirect:/sjhealthy/board";
        }
    }

    public void saveFile(MultipartFile file, BoardDTO boardDTO) throws IOException {
        String projectPath = System.getProperty("user.dir") + "/uploads/files";
//            "/src/main/resources/static/files"; // 이렇게 하면 내부라 웹에서 직접 접근 못함

        UUID uuid = UUID.randomUUID();
        String fileOriginName = file.getOriginalFilename();
        String fileName = uuid + "_" + fileOriginName; // 저장용 이름 만들어줌

        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        boardDTO.setBoardFileName(fileName); // DB에 추가
        boardDTO.setBoardFilePath("/uploads/files/" + fileName);
    }

    @ResponseBody
    @GetMapping("/uploads/files/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws UnsupportedEncodingException, MalformedURLException {
        // 파일 경로
        File file = new File("uploads/files/" + fileName);

        if (!file.exists()){
            return ResponseEntity.notFound().build();
        }
        // 파일 URL 생성
        String fileUrl = "http://localhost:8081/sjhealthy/uploads/files/" + fileName;
        // 파일을 resource로 감싸서 반환, URL만으로 이미지를 표시 가능
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(resource);
    }

    @RequestMapping("/board/read")
    public String readPost(@RequestParam(name="boardId") Long boardId, Model model, @RequestParam(name= "page", defaultValue = "1") int page,
                           @SessionAttribute(name = "loginId", required = false) String loginId, RedirectAttributes ra,
                           HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("loginId", loginId);

        try {
            BoardDTO result = boardService.read(boardId);

            if (result.getBoardFileName() != null && !result.getBoardFileName().isEmpty()) { // 첨부파일 있을 때 출력
                model.addAttribute("attachedFile", result.getBoardFileName());
            }

            // 조회수 브라우저 종료시 다시 집계됨
            Cookie viewCount = addViews(request, boardId, result);
            if (viewCount != null){
                response.addCookie(viewCount);
            }

            if (loginId != null){ // 로그인 한 상태라면
                // 관리자
                MemberDTO dto = memberService.findMemberIdAtPassFind(loginId);

                if (dto.getMemberAuth().equals("A")){ // 관리자
                    model.addAttribute("admin", dto.getMemberAuth());
                }
            }

            //댓글 목록 가져오기
            List<CommentDTO> commentDTOList = commentService.findAll(boardId);
            model.addAttribute("commentList", commentDTOList);

            // 날짜 yy/MM/dd로
            String date = result.getCreateDate();
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);
            String sendDate = year + "/" + month + "/" + day;

            model.addAttribute("boardDTO", result);
            model.addAttribute("createDate", sendDate);
            model.addAttribute("page", page);
            return "board/read";
        } catch (Exception e){
            e.printStackTrace();
            ra.addFlashAttribute("page", page);
            return "redirect:/sjhealthy/board";
        }
    }

    public Cookie addViews(HttpServletRequest request, Long boardId, BoardDTO boardDTO){
        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            boardService.countBoardView(boardDTO);
            // 쿠키가 없다면 생성
            Cookie newCookie = new Cookie("view_count", boardId.toString());
            newCookie.setPath("/");
            newCookie.setMaxAge(24 * 60 * 60); // 24시간
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
        newCookie.setMaxAge(24 * 60 * 60); // 24시간
        return newCookie;
    }

    @GetMapping("/board/update")
    public String getUpdateForm(@RequestParam("boardId") Long boardId, Model model,
                                @SessionAttribute(name = "loginId", required = false) String loginId,
                                @RequestParam("page") int page, RedirectAttributes ra){
        model.addAttribute("loginId", loginId);

        if (loginId == null){
            ra.addFlashAttribute("message", "로그인 후 이용해 주세요");
            return "redirect:/sjhealthy/board";
        } else {
            // 회원은 글 수정 뷰로 연결
            BoardDTO dto = boardService.read(boardId);
            model.addAttribute("loginId", loginId);
            model.addAttribute("isUpdate", dto);
            model.addAttribute("page", page);

            // 첨부파일 존재하면
            String fileName = dto.getBoardFileName();
            if (fileName != null){
                model.addAttribute("fileName", fileName.substring(fileName.indexOf("_") + 1));
                // 저장용 이름 말고 원래 첨부한 이름으로 보내서 사용
            }
            return "board/write"; // 뷰에서 확인하여 다른 폼 출력
        }
    }

    @PostMapping("/board/update")
    public String updatePost(@ModelAttribute BoardDTO boardDTO, Model model, RedirectAttributes ra,
                             @SessionAttribute(name = "loginId", required = false) String loginId,
                             @RequestParam("boardId") Long boardId, @RequestParam("file") MultipartFile file,
                             @RequestParam(name = "page") int page){
        model.addAttribute("loginId", loginId);

        // 업데이트일로 저장할 날짜 불러오기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDate.now().format(formatter);

        try {
            // 기존 게시글 데이터에 변경된 데이터 추가
            BoardDTO postDTO = boardService.read(boardId);
            postDTO.setBoardTitle(boardDTO.getBoardTitle());
            postDTO.setBoardContent(boardDTO.getBoardContent());
            postDTO.setUpdateUser(loginId);
            postDTO.setUpdateDate(today);

            assert file != null; // nullPointerException 발생 막기 위해
            if (!file.isEmpty()){ // 첨부파일이 존재한다면
                // 기존에 첨부되었던 파일이 있다면 삭제해준다.
                if (postDTO.getBoardFileName() != null){
                    File before = new File(postDTO.getBoardFilePath());

                    if (before.exists()){
                        if (before.delete()){
                            System.out.println("기존 첨부 파일이 삭제되었습니다.");
                        } else System.out.println("첨부파일이 삭제되지 않았습니다.");
                    }
                }
                saveFile(file, postDTO); // postDTO에 MultipartFile로 읽어온 첨부파일 저장
            }
            // 새로운 데이터로 다시 등록
            BoardDTO updateResult = boardService.write(postDTO);

            if (updateResult != null) {
                ra.addAttribute("boardId", boardDTO.getBoardId());
                ra.addFlashAttribute("message", "글이 수정되었습니다.");
                ra.addFlashAttribute("boardDTO", boardDTO);
                ra.addAttribute("page", page);

                return "redirect:/sjhealthy/board/read";
            } else {
                ra.addAttribute("boardId", boardDTO.getBoardId());
                ra.addFlashAttribute("message", "글 수정에 실패했습니다.");
                ra.addAttribute("page", page);

              return "redirect:/sjhealthy/board/read";
            }
        } catch (Exception e){
            e.printStackTrace(); // 오류 떠서 이유 확인용
            ra.addFlashAttribute("message", "시스템 오류로 글 수정에 실패했습니다.");
            return "redirect:/sjhealthy";
        }
    }

    @RequestMapping("/board/delete")
    public String deletePost(@RequestParam("boardId") Long boardId, RedirectAttributes ra,
                             @RequestParam(name="page", defaultValue = "1")int page) throws NullPointerException{
        BoardDTO data = boardService.read(boardId);

        File file = null;
        if (data != null){
            //첨부파일 있는지 확인하기 위해
            if (data.getBoardFileName() != null){
               file = new File(data.getBoardFilePath());
            }
        }

        boolean isDeleted = boardService.delete(boardId);
        if (isDeleted){
            System.out.println("글 삭제가 완료되었습니다.");
            if (file != null && file.exists()){ // 있으면 저장소에서도 삭제
                if (file.delete()){
                    System.out.println("첨부파일이 삭제되었습니다.");
                } else System.out.println("첨부파일이 삭제되지 않았습니다.");
            } else System.out.println("첨부파일이 존재하지 않습니다.");

            ra.addFlashAttribute("message", "글 삭제가 완료되었습니다.");
            ra.addFlashAttribute("page", page);
            return "redirect:/sjhealthy/board";
        } else {
            ra.addFlashAttribute("message", "글 삭제에 실패했습니다.");
            ra.addAttribute("boardId", boardId);
            return "redirect:/sjhealthy/board/read";
        }
    }

    // 관리자 기능에서 사용
    @ResponseBody
    @RequestMapping("/board/delete/{boardId}")
    public ResponseEntity<Response<Object>> deletePostForAdmin(@PathVariable("boardId") Long boardId) throws NullPointerException{
        BoardDTO data = boardService.read(boardId);
        if (data != null) {
            //첨부파일 있는지 확인하기 위해
            String filePath = data.getBoardFilePath();
            if (filePath != null && !filePath.isEmpty()) {
                File file = new File(filePath);
                boolean isDeleted = boardService.delete(boardId);
                if (isDeleted) {
                    System.out.println("글 삭제가 완료되었습니다.");
                    assert file != null; // nullPointerException 발생 막기 위해
                    if (file.exists()) { // 있으면 저장소에서도 삭제
                        if (file.delete()) {
                            System.out.println("첨부파일이 삭제되었습니다.");
                        } else System.out.println("첨부파일이 삭제되지 않았습니다.");
                    } else System.out.println("첨부파일이 존재하지 않습니다.");
                    return ResponseEntity.ok(new Response<>(null, "글 삭제가 완료되었습니다."));
                } else {
                    System.out.println("글 삭제에 실패했습니다.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "글 삭제에 실패했습니다."));
                }
            } else {
                System.out.println("첨부파일 경로가 없으므로 파일 삭제를 생략합니다.");
                // 첨부파일 경로가 없을 경우 별도 처리
                boolean isDeleted = boardService.delete(boardId);
                if (isDeleted) {
                    System.out.println("글 삭제가 완료되었습니다.");
                    return ResponseEntity.ok(new Response<>(null, "글 삭제가 완료되었습니다."));
                } else {
                    System.out.println("글 삭제에 실패했습니다.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "글 삭제에 실패했습니다."));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(null, "글이 존재하지 않습니다."));
    }
}
