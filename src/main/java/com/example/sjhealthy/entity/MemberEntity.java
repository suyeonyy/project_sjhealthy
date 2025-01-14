package com.example.sjhealthy.entity;

import com.example.sjhealthy.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 활성화
@Table(name = "member_table")
public class MemberEntity {
    @Id
    @Column(name="member_id", columnDefinition = "VARCHAR(500)", nullable = false)
    private String memberId;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String memberPassword;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String memberName;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String memberPnum;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String memberEmail;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String memberBirth;

    @Column(columnDefinition = "VARCHAR(2)", nullable = false)
    private String memberGender;

    @Column(columnDefinition = "NUMERIC(18, 1)")
    private Double memberHeight;   // 키(통계에 사용, 가입 후 따로 받음)

    @Column(columnDefinition = "VARCHAR(2) DEFAULT 'U'", nullable = false)
    private String memberAuth;

    @Column(columnDefinition = "VARCHAR(2) DEFAULT 'N'", nullable = false)
    private String isDeleted;

    @Column(name="create_date", columnDefinition = "VARCHAR(8)", nullable = false)
    private String createDate;

    @Column(name="update_date", columnDefinition = "VARCHAR(8)", nullable = false)
    private String updateDate;

    @Column(name="create_user", columnDefinition = "VARCHAR(500)", nullable = false)
    private String createUser;

    @Column(name="update_user", columnDefinition = "VARCHAR(500)", nullable = false)
    private String updateUser;

    @JsonIgnore // 무한 재귀 방지
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardEntity> board;  // 하나의 회원이 여러 게시글을 가짐

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DailyEntity> daily;  // 하나의 회원이 여러 일지글을 가짐

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecommendEntity> recommend;  // 하나의 회원이 여러 추천글을 가짐


    @PrePersist
    public void prePersist(){
        if (this.createDate == null){
            // 날짜 저장 형식 지정하여 현재 날짜 저장
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            this.createDate = LocalDate.now().format(formatter);
        }
        // 수정시 날짜 저장 형식 지정하여 현재 날짜 저장
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.updateDate = LocalDate.now().format(formatter);   // 수정일 저장
//        this.updateDate = this.createDate; 이렇게 해도 됨


        // memberId 넣어줌 (memberId null 값 확인 후, defaultUser 설정)
        if (this.createUser == null){
            this.createUser = this.memberId != null ? this.memberId : "defaultUser";
            this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
        }

        // 등록자, 수정자 다를 경우, 수정자에 최근 등록자 대입
        if (!this.createUser.equals(this.memberId)){
            this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
        }


        if (this.isDeleted == null){
            this.isDeleted = "N";
        }

        if (this.memberAuth == null){
            this.memberAuth = "U";
        }

        if (this.memberHeight == null){
            this.memberHeight = 0.0;
        }
    }
    @PreUpdate
    public void preUpdate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.updateDate = LocalDate.now().format(formatter);
    }
}
