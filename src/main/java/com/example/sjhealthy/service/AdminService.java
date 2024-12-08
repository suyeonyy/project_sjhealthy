package com.example.sjhealthy.service;

import com.example.sjhealthy.component.MemberMapper;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;

    public List<MemberDTO> getMemberList(){
        List<MemberEntity> memberEntityList= memberRepository.findAll();

        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity entity : memberEntityList){
            MemberDTO dto = MemberMapper.toMemberDTO(entity);
            memberDTOList.add(dto);
        }
        return memberDTOList;
    }

    public boolean deleteMember(String memberId){
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);

        if (memberEntity.isPresent()){
            memberRepository.deleteById(memberId);
            return true;
        } else return false;
    }
    // TODO: 신고 관련 테이블

}
