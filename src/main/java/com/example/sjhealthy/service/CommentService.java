package com.example.sjhealthy.service;

import com.example.sjhealthy.dto.CommentDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.CommentEntity;
import com.example.sjhealthy.repository.BoardRepository;
import com.example.sjhealthy.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository; //부모 엔티티도 넘기기 위해

    public Long save(CommentDTO commentDTO) {
        //dto로 받아온 것을 entity로 변환

        /* 부모엔티티(BoardEntity) 조회 */
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());

        //존재한다면 댓글 저장 처리
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);

            //변환해온 것을 저장
            return commentRepository.save(commentEntity).getComId();
        }else{
            return null;
        }
    }

    /*게시글 id로 전체 댓글 조회*/
    public List<CommentDTO> findAll(Long boardId) {
        // select from comment_table where 1 = 1  and board_id=? order by id desc;
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        //호출 결과를 EntityList로 받는다.
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByComIdDesc(boardEntity);
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>(); //commentDTOList에 댓글을 하나씩 담는다.
        for(CommentEntity commentEntity: commentEntityList){
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
