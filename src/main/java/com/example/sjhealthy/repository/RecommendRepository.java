package com.example.sjhealthy.repository;


import com.example.sjhealthy.entity.RecommendEntity;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

//@Repository 생략 가능
public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> { // <Entity클래스, PK타입>

    @Query("SELECT " +
            "(LENGTH(r.recY) - LENGTH(REPLACE(r.recY, '_', ''))) AS likeCount, " +
            "(LENGTH(r.recN) - LENGTH(REPLACE(r.recN, '_', ''))) As dislikeCount " +
            "FROM RecommendEntity r WHERE r.recId = :recId") // :는 동적 바인딩으로 값이 들어갈 파라미터 자리를 표시하는 것
            //이때 FROM 뒤엔 테이블 명이 아니라 엔티티 클래스 명을 사용한다.
    List<Tuple> getLikeDislikeCount(@Param("recId")Long recId);
/*
    @Query("SELECT *" +
        "FROM RecommendEntity r" +
        "WHERE r.recStoreId = :recStoreId" +
        "   OR r.recStore = :recStore ") // 고유번호가 같거나 가게 이름이 같을 때(가게이름은 프랜차이즈 때문에 같이 검사)
    Set<RecommendEntity> getRecommendationByStoreNameOrStoreId(@Param("recStoreId") Long recStoreId,
                                                               @Param("recStore") String recStore);
 */
}
