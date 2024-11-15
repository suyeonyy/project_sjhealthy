package com.example.sjhealthy.service;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDTO login(MemberDTO memberDTO) {
        //DB에 계정이 있는지 확인
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberDTO.getMemberId());

        if (byMemberId.isPresent()){
            MemberEntity memberEntity = byMemberId.get(); //get: Optional 껍데기 벗기기?

            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                return null; // 로그인 실패
            }
        } else {
            return null;
        }
    }
}