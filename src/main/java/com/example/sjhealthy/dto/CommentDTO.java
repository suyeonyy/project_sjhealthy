package com.example.sjhealthy.dto;

import com.example.sjhealthy.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long comId;             //댓글 고유번호
    private String memberId;        //아이디
    private String commentWriter;   //작성자
    private String commentContent;  //내용
    private String isDeleted;       //삭제여부
    private String createDate;      //등록일
    private String updateDate;      //수정일
    private String createUser;      //등록자
    private String updateUser;      //수정자
    private Long boardId;           //글 고유번호
    private Long commentOrder; // 댓글 순서 (게시판별로 순차적인 번호)

    private String formattedCreateDate; // 화면에 출력될 포맷된 날짜

    // getter, setter 메서드 추가
    public String getFormattedCreateDate() {
        if (this.createDate != null) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(createDate, inputFormatter);
            return date.format(outputFormatter);
        }
        return null; // createDate가 null일 경우
    }

    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long boardId) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setComId(commentEntity.getComId());
        commentDTO.setMemberId(commentEntity.getMemberId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContent(commentEntity.getCommentContent());
        commentDTO.setIsDeleted(commentEntity.getIsDeleted());
        commentDTO.setCreateDate(commentEntity.getCreateDate());
        commentDTO.setUpdateDate(commentEntity.getUpdateDate());
        commentDTO.setCreateUser(commentEntity.getCreateUser());
        commentDTO.setUpdateUser(commentEntity.getUpdateUser());
        commentDTO.setCommentOrder(commentEntity.getCommentOrder());
        commentDTO.setBoardId(boardId);

        return commentDTO;
    }
}
