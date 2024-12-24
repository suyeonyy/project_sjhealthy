package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.CommentDTO;
import com.example.sjhealthy.dto.Response;
import com.example.sjhealthy.repository.CommentRepository;
import com.example.sjhealthy.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sjhealthy/")
public class CommentConroller {
    private final CommentService commentService;

    @PostMapping("/comment/save")
    //public @ResponseBody save(@ModelAttribute CommentDTO commentDTO){ ajax사용 시 @ResponseBody 사용
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO,
                               @SessionAttribute(name = "loginId", required = false)String loginId){ //ResponseEntity: body와 header를 같이 다룰 수 있는 객체
        System.out.println("commentDTO="+commentDTO);

        Long saveResult = commentService.save(commentDTO);

        if(saveResult != null){
            //작성 성공
            //다시 댓글 전체를 가지고 와서 보여주기(리턴)
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId()); //게시글 번호

            //기존에 사용하던
            //return new ResponseEntity<>(commentDTOList, HttpStatus.OK); //body, 상태코드값
            //ResponseEntity에서 Response 클래스 사용하여 응답하는 방법
            return ResponseEntity.ok(new Response<>(commentDTOList, null));

            //ResponseEntity에서 Response 클래스 사용하여 응답하는 방법
            //관리자의 권한만 볼 수 있는데, 관리자가 아닌 사람이 접근할 경우 아래와 같은 형식으로 출력하기
            //return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response<>(commentDTOList, null)); //body, 상태코드값
        }else{
            //작성 실패

            //ResponseEntity에서 Response 클래스 사용하여 응답하는 방법
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(null,"해당 게시글이 존재하지 않습니다."));

            //기존에 사용하던
            //return new ResponseEntity<>( "해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

}
