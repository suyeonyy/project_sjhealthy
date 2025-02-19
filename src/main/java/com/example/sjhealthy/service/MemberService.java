package com.example.sjhealthy.service;

import com.example.sjhealthy.component.BoardMapper;
import com.example.sjhealthy.component.MemberMapper;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    //레파지토리 호출
    private final MemberRepository memberRepository;

    private final PasswordEncoder pwEncoder;

    public List<MemberDTO> getMemberList(){
        List<MemberEntity> memberList = memberRepository.findAll();

        List<MemberDTO> dtoList = new ArrayList<>();
        for (MemberEntity entity : memberList){
            MemberDTO dto = MemberMapper.toMemberDTO(entity);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public Page<MemberDTO> getMemberListWithPage(int page, int size){
        Pageable pageable = PageRequest.of(page-1, size); // page는 1부터 시작하니까 -1 해서 가져옴

        Page<MemberEntity> entity = memberRepository.findAllByOrderByCreateDateDesc(pageable);
        return MemberMapper.convertToMemberDTOPage(entity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        //DB에 계정이 있는지 확인
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberDTO.getMemberId());

        if (byMemberId.isPresent()){
            MemberEntity memberEntity = byMemberId.get(); //get: Optional 껍데기 벗기기

            String rawPw = ""; // 사용자가 제출한 비밀번호
            String encodePw = memberEntity.getMemberPassword(); // DB에 저장된 암호화된 비밀번호
            rawPw = memberDTO.getMemberPassword();

            if(pwEncoder.matches(rawPw, encodePw)) {   //비밀번호 일치 여부 판단
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                MemberDTO dto = MemberMapper.toMemberDTO(memberEntity);
                return dto;
            } else {
                return null; // 로그인 실패
            }
        } else {
            return null;
        }
    }

    public void join(MemberDTO memberDTO) {
        //비밀번호 암호화
        String encryptedPassword = pwEncoder.encode(memberDTO.getMemberPassword());

        // MemberDTO에서 MemberEntity로 변환
        MemberEntity memberEntity = MemberMapper.toMemberEntity(memberDTO);

        // 암호화된 비밀번호를 Entity에 설정
        memberEntity.setMemberPassword(encryptedPassword);

        memberRepository.save(memberEntity);
    }

    public void changePassword(MemberDTO memberDTO){
        // 기존 엔티티 조회
        Optional<MemberEntity> entity = memberRepository.findByMemberId(memberDTO.getMemberId());

        if (entity.isPresent()){
            MemberEntity memberEntity = entity.get();

            // 비밀번호만 교체
            memberEntity.setMemberPassword(pwEncoder.encode(memberDTO.getMemberPassword()));

            // 교체한 걸로 등록
            memberRepository.save(memberEntity);
        }
    }

    public void addMemberHeight(Double height, MemberDTO memberDTO){
        // 기존 엔티티 조회
        Optional<MemberEntity> entity = memberRepository.findByMemberId(memberDTO.getMemberId());

        if (entity.isPresent()){
            MemberEntity memberEntity = entity.get();

            // 키 추가
            memberEntity.setMemberHeight(height);

            // 교체한 걸로 등록
            memberRepository.save(memberEntity);
        }
    }

    /* 회원가입 아이디 중복체크 */
    public int memberIdCheck(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            return 1;
        }

        // memberId로 회원을 조회
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberId(memberId);

        // 회원이 존재하면 1, 존재하지 않으면 0을 반환
        return memberEntity.isPresent() ? 1 : 0;
    }

    public MemberDTO findMemberId(MemberDTO memberDTO){ // 아이디 찾기에서 사용
        //jpa에서 optional 타입으로 받는다
        Optional<MemberEntity> byMemberId =
            memberRepository.findByMemberNameAndMemberBirth(memberDTO.getMemberName(), memberDTO.getMemberBirth());

        if (byMemberId.isPresent()){
            //Optional 타입을 Entity타입으로 바꾸기 위해 .get()을 붙임
            MemberEntity memberEntity = byMemberId.get();
            return MemberMapper.toMemberDTO(memberEntity);
        } else return null;
    }

    public MemberDTO findMemberIdAtPassFind(String memberId){ // 비밀번호 찾기에서 사용. 가장 기본적인 형태의 아이디로 찾기 메서드
        Optional<MemberEntity> byMemberId = memberRepository.findById(memberId);

        if (byMemberId.isPresent()){
            MemberEntity memberEntity = byMemberId.get();
            return MemberMapper.toMemberDTO(memberEntity);
        } else return null;
    }

    public MemberDTO findMemberEmail(String memberEmail){
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);

        if (byMemberEmail.isPresent()){
            MemberEntity memberEntity = byMemberEmail.get();
            return MemberMapper.toMemberDTO(memberEntity);
        } else return null;
    }

    public void delete(String memberId) {
        Optional<MemberEntity> result = memberRepository.findByMemberId(memberId);
        if (result.isPresent()) { // 아이디가 존재하면 지움
            memberRepository.deleteById(memberId);
        }
    }

    public MemberEntity findMemberEntity(String loginId){ // 아이디 찾기에서 사용
        // loginId를 이용하여 MemberEntity를 조회
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(loginId);
        MemberEntity memberEntity = byMemberId.get();
        return memberEntity;
    }
}
