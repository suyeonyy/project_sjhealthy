package com.example.sjhealthy.component;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.entity.BoardEntity;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {
    public static BoardEntity toBoardEntity(BoardDTO boardDTO){
        BoardEntity boardEntity = new BoardEntity();

        boardEntity.setBoardId(boardDTO.getBoardId());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setMemberId(boardDTO.getMemberId());
        boardEntity.setBoardViews(boardDTO.getBoardViews());
        boardEntity.setBoardContent(boardDTO.getBoardContent());
        boardEntity.setBoardFile(boardDTO.getBoardFile());
        boardEntity.setCreateDate(boardDTO.getCreateDate());
        boardEntity.setUpdateDate(boardDTO.getUpdateDate());
        boardEntity.setCreateUser(boardDTO.getCreateUser());
        boardEntity.setUpdateUser(boardDTO.getUpdateUser());
        boardEntity.setIsDeleted(boardDTO.getIsDeleted());

        return boardEntity;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();

        boardDTO.setBoardId(boardEntity.getBoardId());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setMemberId(boardEntity.getMemberId());
        boardDTO.setBoardViews(boardEntity.getBoardViews());
        boardDTO.setBoardContent(boardEntity.getBoardContent());
        boardDTO.setBoardFile(boardEntity.getBoardFile());
        boardDTO.setCreateDate(boardEntity.getCreateDate());
        boardDTO.setUpdateDate(boardEntity.getUpdateDate());
        boardDTO.setCreateUser(boardEntity.getCreateUser());
        boardDTO.setUpdateUser(boardEntity.getUpdateUser());
        boardDTO.setIsDeleted(boardEntity.getIsDeleted());

        return boardDTO;
    }
}
