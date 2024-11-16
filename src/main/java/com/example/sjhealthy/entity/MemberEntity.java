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
    /*
    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
     */

    //@Column(unique = true) // 제약조건 추가

//
//    @Id // pk 지정
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
//    private String id;

    // TODO: 가입일, 수정일 필요
    @Id
//    @Column(unique = true) // 제약조건 추가
    private String memberId;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    @Column
    private String memberPnum;

    @Column
    private String memberEmail;

    @Column
    private String memberBirth;

    @Column
    private String memberGender;

    @Column
    private String auth;

    @Column
    private String IsExist;


    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPnum(memberDTO.getMemberPnum());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberBirth(memberDTO.getMemberBirth());
        memberEntity.setMemberGender(memberDTO.getMemberGender());

        return memberEntity;
    }
}
