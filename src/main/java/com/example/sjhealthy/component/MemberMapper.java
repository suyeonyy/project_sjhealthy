package com.example.sjhealthy.component;

import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        memberEntity.setMemberHeight(memberDTO.getMemberHeight());
        memberEntity.setMemberDivision(memberDTO.getMemberDivision());
        memberEntity.setMemberAuth(memberDTO.getMemberAuth());
        memberEntity.setIsDeleted(memberDTO.getIsDeleted());
        memberEntity.setCreateDate(memberDTO.getCreateDate());
        memberEntity.setUpdateDate(memberDTO.getUpdateDate());
        memberEntity.setCreateUser(memberDTO.getCreateUser());
        memberEntity.setUpdateUser(memberDTO.getUpdateUser());

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
        memberDTO.setMemberHeight(memberEntity.getMemberHeight());
        memberDTO.setMemberDivision(memberEntity.getMemberDivision());
        memberDTO.setMemberAuth(memberEntity.getMemberAuth());
        memberDTO.setIsDeleted(memberEntity.getIsDeleted());
        memberDTO.setCreateDate(memberEntity.getCreateDate());
        memberDTO.setUpdateDate(memberEntity.getUpdateDate());
        memberDTO.setCreateUser(memberEntity.getCreateUser());
        memberDTO.setUpdateUser(memberEntity.getUpdateUser());

        return memberDTO;

    }

    public static Page<MemberDTO> convertToMemberDTOPage(Page<MemberEntity> memberEntities){
        List<MemberDTO> memberDTOList = memberEntities.stream()
            .map(entity -> {
                String memberId = entity.getMemberId() != null ? entity.getMemberId() : null;

                return new MemberDTO(
                    entity.getMemberId(),
                    entity.getMemberName(),
                    entity.getMemberEmail(),
                    entity.getCreateDate(),
                    entity.getMemberGender(),
                    entity.getMemberBirth()
                );
            })
            .collect(Collectors.toList());

        return new PageImpl<>(memberDTOList, memberEntities.getPageable(), memberEntities.getTotalElements());
    }

    /*
    public static int memberIdCheck(String memberId) {
        return 0;
    }
    */
}
