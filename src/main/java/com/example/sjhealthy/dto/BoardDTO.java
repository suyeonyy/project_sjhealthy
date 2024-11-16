package com.example.sjhealthy.dto;

import com.example.sjhealthy.entity.BoardEntity;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDTO {
    private Long boardId;
    private String boardTitle;
    private String memberId;
    private int boardViews;
    private String boardContent;
    private String File;
    private String credat;
    private String moddat;
    private String creusr;
    private String modusr;
    private String isDeleted;

}
