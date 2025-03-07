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

    @Query(value ="SELECT * " + // 특정 필드만 받으면 Object[]로 반환됨. 그래서 엔티티 자체를 추출
        "FROM recommend_table r " +
        "ORDER BY (LENGTH(r.recY) - LENGTH(REPLACE(r.recY, '_', ''))) DESC " + // 좋아요 수 기준 내림차순 정렬
        "LIMIT 4", nativeQuery = true) // 상위 4개만 가져오기
    List<RecommendEntity> findTop4();

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

    // 검색에 사용
    @Query(value= "SELECT r " + // 특정 필드만 받으면 Object[]로 반환됨. 그래서 엔티티 자체를 추출
        "FROM RecommendEntity r " +
        "WHERE r.recStore LIKE %:recStore% " +
        "ORDER BY r.recId DESC")
    List<RecommendEntity> getRecommendationByStoreName(@Param("recStore") String recStore);

    // 검색 & 페이지네이션
    @Query(value= "SELECT r " + // 특정 필드만 받으면 Object[]로 반환됨. 그래서 엔티티 자체를 추출
        "FROM RecommendEntity r " +
        "WHERE r.recStore LIKE %:recStore% " +
        "ORDER BY r.recId DESC")
    Page<RecommendEntity> getRecommendationByStoreNameWithPage(@Param("recStore") String recStore, Pageable pageable);

    @Query("SELECT r " + // 특정 필드만 받으면 Object[]로 반환됨. 그래서 엔티티 자체를 추출
        "FROM RecommendEntity r " +
        "WHERE (r.recStoreId = :recStoreId " +
        "   OR r.recStore LIKE %:recStore%)" +
        "   AND r.recId IN (" + // 좋아요 5개 이상인 것만 가져옴 // = 는 1개의 값만 비교 가능해서 IN을 사용
        "           SELECT re.recId " +
        "           FROM RecommendEntity re " +
        "           WHERE (LENGTH(re.recY) - LENGTH(REPLACE(re.recY, '_', ''))) >= 5)")
    List<RecommendEntity> getRecommendationByStoreNameOrStoreId(@Param("recStoreId") String recStoreId,
                                                                @Param("recStore") String recStore);

}
