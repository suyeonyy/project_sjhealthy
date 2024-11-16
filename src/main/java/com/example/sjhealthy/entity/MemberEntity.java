package com.example.sjhealthy.entity;

import com.example.sjhealthy.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 활성화
@Table(name = "member_table")
public class MemberEntity {
    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
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

    @Column(columnDefinition = "VARCHAR(2) DEFAULT 'U'", nullable = false)
    private String memberAuth;

    @Column(columnDefinition = "VARCHAR(2) DEFAULT 'N'", nullable = false)
    private String isDeleted;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String createDate;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String updateDate;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String createUser;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String updateUser;

    @PrePersist
    public void prePersist(){
        if (this.createDate == null){
            // 날짜 저장 형식 지정하여 현재 날짜 저장
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            this.createDate = LocalDate.now().format(formatter);

            if (this.updateDate == null){
                // 수정일이 null 이면, 등록일과 동일하게 저장
                this.updateDate = this.createDate;
            }
        }

        // memberId 넣어줌 (memberId null 값 확인 후, defaultUser 설정)
        if (this.createUser == null){
            this.createUser = this.memberId != null ? this.memberId : "defaultUser";
            this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
        }

        // 등록자, 수정자 다를 경우, 수정자에 최근 등록자 대입
        if (!this.createUser.equals(this.memberId)){
            this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
        }
    }

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPnum(memberDTO.getMemberPnum());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberBirth(memberDTO.getMemberBirth());
        memberEntity.setMemberGender(memberDTO.getMemberGender());
        memberEntity.setMemberAuth(memberDTO.getMemberAuth());
        memberEntity.setIsDeleted(memberDTO.getIsDeleted());

        return memberEntity;
    }
}
