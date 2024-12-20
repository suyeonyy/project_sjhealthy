package com.example.sjhealthy.entity;

import com.example.sjhealthy.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@Table(name="comment_table")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comId;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String memberId;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String commentWriter;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String commentContent;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT 'N'", nullable = false) // 형식, 기본값 지정
    private String isDeleted = "N"; // columnDefinition이 적용이 안 될 수 있어 이중으로 기본값 설정 -> 그래도 null이 뜸..

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String createDate;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String updateDate;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String createUser;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String updateUser;

    /* Board:Comment = 1:N */
    //Comment는 Board를 참조하는 관계로 지정한다.
    @ManyToOne(fetch = FetchType.LAZY) //댓글 기준
    @JoinColumn(name="boardId")
    private BoardEntity boardEntity;

    public static CommentEntity toSaveEntity(CommentDTO commentDTO, BoardEntity boardEntity) {
        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setComId(commentDTO.getComId());
        commentEntity.setMemberId(commentDTO.getMemberId());
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContent(commentDTO.getCommentContent());
        commentEntity.setIsDeleted(commentDTO.getIsDeleted());
        commentEntity.setCreateDate(commentDTO.getCreateDate());
        commentEntity.setUpdateDate(commentDTO.getUpdateDate());
        commentEntity.setCreateUser(commentDTO.getCreateUser());
        commentEntity.setUpdateUser(commentDTO.getUpdateUser());
        commentEntity.setBoardEntity(boardEntity);

        return commentEntity;
    }

    @PrePersist
    public void prePersist() {
        /*등록일, 수정일*/
        if (this.createDate == null) {
            // 날짜 저장 형식 지정하여 현재시간 저장
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            this.createDate = LocalDate.now().format(formatter);
        }

        if (this.updateDate == null) {
            //등록일과 동일하게 저장
            this.updateDate = this.createDate;
        }

        /*등록자, 수정자*/
        if (this.createUser == null) {
            this.createUser = this.memberId != null ? this.memberId : "defaultUser";
        }

        //수정자는 무조건 업데이트
        this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
        /*
        if (this.updateUser == null) {
            this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
        }
        */

        /*삭제여부*/
        if(this.isDeleted == null){
            this.isDeleted = "N";
        }
    }

    // 화면 출력용 포맷을 적용한 getter
    @Transient
    public String getFormattedCreateDate() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(createDate, inputFormatter);
        return date.format(outputFormatter);
    }
}
