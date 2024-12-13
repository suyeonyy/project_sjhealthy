package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.CommentDTO;
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
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK); //body, 상태코드값
        }else{
            //작성 실패
            return new ResponseEntity<>( "해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

}
