package com.example.sjhealthy.component;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();

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

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberPnum(memberEntity.getMemberPnum());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberBirth(memberEntity.getMemberBirth());
        memberDTO.setMemberGender(memberEntity.getMemberGender());
        memberDTO.setMemberAuth(memberEntity.getMemberAuth());
        memberDTO.setIsDeleted(memberEntity.getIsDeleted());

        return memberDTO;
<<<<<<< HEAD
=======
    }

    public static int memberIdCheck(String memberId) {
        return 0;
>>>>>>> a976da0a68a9bbb5a8697b597bca07c186035c6d
    }
}
