package com.example.sjhealthy.dto;

import com.example.sjhealthy.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor //기본 생성자 자동 생성
@AllArgsConstructor //필드를 모두 다 매개변수로 하는 생성자 생성
@ToString //DTO 객체가 가진 필드값 출력 시, ToString 매서드를 자동으로 만들어주는 lombok 메서드
public class MemberDTO { //회원 정보에 필요한 내용 필드로 정의
    private String memberId; //계정
    private String memberPassword; //패스워드
    private String memberName; //사용자 명
    private String memberPnum; //전화번호
    private String memberEmail; //이메일
    private String memberBirth; //생년월일
    private String memberGender; //성별

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberPnum(memberEntity.getMemberPnum());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberBirth(memberEntity.getMemberBirth());
        memberDTO.setMemberGender(memberEntity.getMemberGender());

        return memberDTO;
    }
}
