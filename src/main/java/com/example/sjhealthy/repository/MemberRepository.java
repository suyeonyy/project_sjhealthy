package com.example.sjhealthy.repository;

import com.example.sjhealthy.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Optional<MemberEntity> findByMemberId(String memberId);

    Optional<MemberEntity> findByMemberNameAndMemberBirth(String memberName, String memberBirth);
}
