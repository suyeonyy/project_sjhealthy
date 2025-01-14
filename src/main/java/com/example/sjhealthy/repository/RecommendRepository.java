package com.example.sjhealthy.repository;


import com.example.sjhealthy.entity.RecommendEntity;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

//@Repository 생략 가능
public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> { // <Entity클래스, PK타입>

    List<RecommendEntity> findAllByOrderByRecIdDesc();

    Page<RecommendEntity> findAllByOrderByRecIdDesc(Pageable pageable);

    // 추천 게시판 글 작성 중복 체크
    @Query(value="SELECT * " +
            "FROM recommend_table r " +
            "WHERE r.rec_store LIKE CONCAT('%', :recStore, '%') AND r.rec_menu = :recMenu", nativeQuery = true)
    RecommendEntity findByRecStoreAndRecMenu(@Param("recStore")String recStore, @Param("recMenu")String recMenu);

    @Query("SELECT " +
            "(LENGTH(r.recY) - LENGTH(REPLACE(r.recY, '_', ''))) AS likeCount, " +
            "(LENGTH(r.recN) - LENGTH(REPLACE(r.recN, '_', ''))) As dislikeCount " +
            "FROM RecommendEntity r WHERE r.recId = :recId") // :는 동적 바인딩으로 값이 들어갈 파라미터 자리를 표시하는 것
            //이때 FROM 뒤엔 테이블 명이 아니라 엔티티 클래스 명을 사용한다.
    List<Tuple> getLikeDislikeCount(@Param("recId")Long recId);

    @Query("SELECT r " + // 특정 필드만 받으면 Object[]로 반환됨. 그래서 엔티티 자체를 추출
        "FROM RecommendEntity r " +
        "WHERE r.recStoreId = :recStoreId " + // 여기 끝에 공백 필요함
        "   OR r.recStore LIKE %:recStore%") // 고유번호가 같거나 가게 이름이 같을 때(가게이름은 프랜차이즈 때문에 같이 검사)
    List<RecommendEntity> getRecommendationByStoreNameOrStoreId(@Param("recStoreId") String recStoreId,
                                                                @Param("recStore") String recStore);

}
