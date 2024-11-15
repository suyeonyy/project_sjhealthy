package com.example.sjhealthy.entity;

import com.example.sjhealthy.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="board_table")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String boardId;

    @Column
    private String memberId;

    @Column
    private int boardViews;

    @Column
    private String boardContent;

    @Column
    private String File;

    @Column
    private Date credat;

    @Column
    private Date moddat;

    @Column
    private String creusr;

    @Column
    private String modusr;

    @Column
    private String isExist;


    public static BoardEntity toBoardEntity(BoardDTO boardDTO){
        BoardEntity boardEntity = new BoardEntity();

        boardEntity.setBoardId(boardDTO.getBoardId());
        boardEntity.setMemberId(boardDTO.getMemberId());
        boardEntity.setBoardViews(boardDTO.getBoardViews());
        boardEntity.setBoardContent(boardDTO.getBoardContent());
        boardEntity.setFile(boardDTO.getFile());
        boardEntity.setCredat(boardDTO.getCredat());
        boardEntity.setModdat(boardDTO.getModdat());
        boardEntity.setCreusr(boardDTO.getCreusr());
        boardEntity.setModusr(boardDTO.getModusr());
        boardEntity.setIsExist(boardDTO.getIsExist());

        return boardEntity;
    }

}
