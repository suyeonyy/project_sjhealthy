package com.example.sjhealthy.dto;

import com.example.sjhealthy.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private String memberId; //계정
    private String memberPassword; //패스워드
    private String memberName; //사용자 명
    private String memberPnum; //전화번호
    private String memberEmail; //이메일
    private String memberBirth; //생년월일
    private String memberGender; //성별
    private String auth; //권한
    private String isExist; //삭제여부

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberPnum(memberEntity.getMemberPnum());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberBirth(memberEntity.getMemberBirth());
        memberDTO.setMemberGender(memberEntity.getMemberGender());

        memberDTO.setAuth(memberEntity.getAuth());
        memberDTO.setIsExist(memberEntity.getIsExist());

        return memberDTO;
    }
}
