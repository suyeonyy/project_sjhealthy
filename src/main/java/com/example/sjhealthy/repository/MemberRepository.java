package com.example.sjhealthy.repository;

import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Page<MemberEntity> findAllByOrderByCreateDateDesc(Pageable pageable); // 페이징 추가한 거

    Optional<MemberEntity> findByMemberId(String memberId);

    Optional<MemberEntity> findByMemberNameAndMemberBirth(String memberName, String memberBirth);

    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
