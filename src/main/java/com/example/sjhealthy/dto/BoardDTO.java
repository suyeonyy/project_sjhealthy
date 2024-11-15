package com.example.sjhealthy.dto;

import com.example.sjhealthy.entity.BoardEntity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDTO {
    private Long boardId;
    private String memberId;
    private int boardViews;
    private String boardContent;
    private String File;
    private Date credat;
    private Date moddat;
    private String creusr;
    private String modusr;
    private String isExist;

    public static BoardDTO toBoardDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();

        boardDTO.setBoardId(boardEntity.getBoardId());
        boardDTO.setMemberId(boardEntity.getMemberId());
        boardDTO.setBoardViews(boardEntity.getBoardViews());
        boardDTO.setBoardContent(boardEntity.getBoardContent());
        boardDTO.setFile(boardEntity.getFile());
        boardDTO.setCredat(boardEntity.getCredat());
        boardDTO.setModdat(boardEntity.getModdat());
        boardDTO.setCreusr(boardEntity.getCreusr());
        boardDTO.setModusr(boardEntity.getModusr());
        boardDTO.setIsExist(boardEntity.getIsExist());

        return boardDTO;
    }
}
