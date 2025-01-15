package com.example.sjhealthy.component;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.MemberEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardMapper {
    public static BoardEntity toBoardEntity(BoardDTO boardDTO, MemberEntity memberEntity){
        // memberId를 통해 MemberEntity를 조회
        BoardEntity boardEntity = new BoardEntity();

        boardEntity.setBoardId(boardDTO.getBoardId());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
//        boardEntity.setMemberId(boardDTO.getMemberId());
        boardEntity.setBoardViews(boardDTO.getBoardViews());
        boardEntity.setBoardContent(boardDTO.getBoardContent());
        boardEntity.setBoardFileName(boardDTO.getBoardFileName());
        boardEntity.setBoardFilePath(boardDTO.getBoardFilePath());
        boardEntity.setCreateDate(boardDTO.getCreateDate());
        boardEntity.setUpdateDate(boardDTO.getUpdateDate());
        boardEntity.setCreateUser(boardDTO.getCreateUser());
        boardEntity.setUpdateUser(boardDTO.getUpdateUser());
        boardEntity.setIsDeleted(boardDTO.getIsDeleted());
        boardEntity.setMember(memberEntity);

        return boardEntity;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity, String memberId){
        BoardDTO boardDTO = new BoardDTO();

        boardDTO.setBoardId(boardEntity.getBoardId());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
//        boardDTO.setMemberId(boardEntity.getMemberId());
        boardDTO.setBoardViews(boardEntity.getBoardViews());
        boardDTO.setBoardContent(boardEntity.getBoardContent());
        boardDTO.setBoardFileName(boardEntity.getBoardFileName());
        boardDTO.setBoardFilePath(boardEntity.getBoardFilePath());
        boardDTO.setCreateDate(boardEntity.getCreateDate());
        boardDTO.setUpdateDate(boardEntity.getUpdateDate());
        boardDTO.setCreateUser(boardEntity.getCreateUser());
        boardDTO.setUpdateUser(boardEntity.getUpdateUser());
        boardDTO.setIsDeleted(boardEntity.getIsDeleted());
        boardDTO.setMemberId(memberId);

        return boardDTO;
    }

    public static Page<BoardDTO> convertToBoardDTOPage(Page<BoardEntity> boardEntities){
        List<BoardDTO> boardDTOList = boardEntities.stream()
            .map(entity -> {
                // BoardEntity에서 MemberDTO의 memberId를 가져와서 BoardDTO에 설정
                String memberId = entity.getMember() != null ? entity.getMember().getMemberId() : null;

                return new BoardDTO(
                    entity.getBoardId(),
                    entity.getBoardTitle(),
                    memberId,
                    entity.getBoardViews(),
                    entity.getBoardContent(),
                    entity.getBoardFileName(),
                    entity.getBoardFilePath(),
                    entity.getCreateDate(),
                    entity.getUpdateDate(),
                    entity.getCreateUser(),
                    entity.getUpdateUser(),
                    entity.getIsDeleted()
                );
            })
            .collect(Collectors.toList());

        return new PageImpl<>(boardDTOList, boardEntities.getPageable(), boardEntities.getTotalElements());
    }
}
