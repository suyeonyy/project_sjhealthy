package com.example.sjhealthy.dto;

import com.example.sjhealthy.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private String memberId;        //계정
    private String memberPassword;  //패스워드
    private String memberName;      //사용자 명
    private String memberPnum;      //전화번호
    private String memberEmail;     //이메일

    private String memberEmailFirst;     //이메일
    private String memberEmailLast;     //이메일


    private String memberBirth;     //생년월일
    private String memberGender;    //성별
    private Double memberHeight;       //키

    private String memberDivision;      //가입 경로
    private String memberAuth;      //권한

    private String isDeleted;       //삭제여부
    private String createDate;      //등록일
    private String updateDate;      //수정일
    private String createUser;      //등록자
    private String updateUser;      //수정자

    public MemberDTO(String memberId, String memberPassword) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
    }

    public MemberDTO(String memberId, String memberName, String memberEmail, String createDate, String memberGender, String memberBirth){
        this(memberId, null, memberName, null, memberEmail, null, null, memberBirth, memberGender, null, null, null, null, createDate, null, null, null);
    }
}
