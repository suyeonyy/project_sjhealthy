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

    /* 기존. 임시주석처리
    public Long save(CommentDTO commentDTO) {
        //dto로 받아온 것을 entity로 변환

        // 부모엔티티(BoardEntity) 조회
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
    */
    public Long save(CommentDTO commentDTO) {
        //dto로 받아온 것을 entity로 변환

        // 부모엔티티(BoardEntity) 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());

        //존재한다면 댓글 저장 처리
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);


            // 댓글이 속할 게시판 번호 (BoardEntity 기준)
            Long boardId = commentDTO.getBoardId();

            // 해당 게시판에 대한 가장 큰 댓글 순서를 가져옴
            Long commentOrder = commentRepository.findMaxCommentOrderByBoardId(boardId);

            // 댓글 순서를 계산 (댓글이 없으면 1번부터 시작)
            commentOrder = commentOrder == null ? 1L : commentOrder + 1;

            // 댓글 순서를 설정
            commentEntity.setCommentOrder(commentOrder);

            // 댓글 저장
            commentRepository.save(commentEntity);

            // 저장된 댓글의 ID 반환
            return commentEntity.getComId();
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
