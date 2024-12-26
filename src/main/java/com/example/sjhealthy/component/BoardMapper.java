package com.example.sjhealthy.component;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.MemberEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

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
}
