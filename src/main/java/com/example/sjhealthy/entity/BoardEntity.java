package com.example.sjhealthy.entity;

import com.example.sjhealthy.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.aspectj.weaver.loadtime.definition.Definition;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="board_table")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false) // 형식과 NOTNULL (nullable은 기본값이 true)
    private String boardTitle;

//    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
//    private String memberId;

    @Column(columnDefinition = "NUMERIC(18,0) DEFAULT 0", nullable = false)
    private int boardViews;

    @Column(columnDefinition = "VARCHAR(2000)", nullable = false)
    private String boardContent;

    @Column(columnDefinition = "VARCHAR(200)")
    private String boardFileName;

    @Column(columnDefinition = "VARCHAR(200)")
    private String boardFilePath;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String createDate;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String updateDate;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String createUser;

    @Column(columnDefinition = "VARCHAR(500)")
    private String updateUser;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT 'N'", nullable = false) // 형식, 기본값 지정
    private String isDeleted = "N"; // columnDefinition이 적용이 안 될 수 있어 이중으로 기본값 설정 -> 그래도 null이 뜸..

    /* Board:Comment = 1:N */
    //게시글이 삭제되면 댓글도 함께 삭제된다
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY) //게시판 기준
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @JsonIgnore // 무한 재귀 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @PrePersist // 날짜 기본 형식 지정하여 DB로
    public void prePersist(){
        if (this.createDate == null){
            // 날짜 저장 형식 지정하여 현재시간 저장
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            this.createDate = LocalDate.now().format(formatter);

            if (this.updateDate == null){
                // 수정일 없으면 작성일과 동일하게 저장
                this.updateDate = this.createDate;
            }
        }

        // creusr에 memberId 넣어줌
        // 이때 memberId가 null 일 수도 있어서 null인지 확인하고 default값도 설정해준다.
        if (this.createUser == null){
            this.createUser = this.member.getMemberId() != null ? this.member.getMemberId() : "defaultUser";
        }

        if (this.updateUser == null){
            this.updateUser = this.member.getMemberId() != null ? this.member.getMemberId() : "defaultUser";
        } else if (!this.createUser.equals(this.member.getMemberId())){ // 생성자 수정자 다르면 수정자에 최근 작성자를 넣음
            this.updateUser = this.member.getMemberId() != null ? this.member.getMemberId() : "defaultUser";
        }


        if (this.isDeleted == null){
            this.isDeleted = "N"; // 그냥 여기에 해주는 게 제일 확실하다
        }
    }
    @PreUpdate
    public void preUpdate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.updateDate = LocalDate.now().format(formatter);
    }

}
