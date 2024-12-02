package com.example.sjhealthy.repository;


import com.example.sjhealthy.entity.RecommendEntity;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository 생략 가능
public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> { // <Entity클래스, PK타입>

    @Query("SELECT " +
            "(LENGTH(r.recY) - LENGTH(REPLACE(r.recY, '_', ''))) AS likeCount, " +
            "(LENGTH(r.recN) - LENGTH(REPLACE(r.recN, '_', ''))) As dislikeCount " +
            "FROM RecommendEntity r WHERE r.recId = :recId") // :는 동적 바인딩으로 값이 들어갈 파라미터 자리를 표시하는 것
            //이때 FROM 뒤엔 테이블 명이 아니라 엔티티 클래스 명을 사용한다.
    List<Tuple> getLikeDislikeCount(@Param("recId")Long recId);
}
