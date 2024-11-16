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
        boardEntity.setFile(boardDTO.getFile());
        boardEntity.setCredat(boardDTO.getCredat());
        boardEntity.setModdat(boardDTO.getModdat());
        boardEntity.setCreusr(boardDTO.getCreusr());
        boardEntity.setModusr(boardDTO.getModusr());
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
        boardDTO.setFile(boardEntity.getFile());
        boardDTO.setCredat(boardEntity.getCredat());
        boardDTO.setModdat(boardEntity.getModdat());
        boardDTO.setCreusr(boardEntity.getCreusr());
        boardDTO.setModusr(boardEntity.getModusr());
        boardDTO.setIsDeleted(boardEntity.getIsDeleted());

        return boardDTO;
    }
}
