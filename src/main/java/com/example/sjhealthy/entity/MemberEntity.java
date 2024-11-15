package com.example.sjhealthy.entity;

import com.example.sjhealthy.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity {

    @Column(unique = true) // 제약조건 추가
    private String memberId;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    @Column
    private String memberBirth;

    @Column
    private String memberPNum;

    @Column
    private String memberEmail;

    @Column
    private String auth;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberBirth(memberDTO.getMemberBirth());
        memberEntity.setMemberPNum(memberDTO.getMemberPNum());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setAuth(memberDTO.getAuth());
        return memberEntity;
    }
}
