package com.example.sjhealthy.repository;


import com.example.sjhealthy.entity.RecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository 생략 가능
public interface RecommendRepository extends JpaRepository<RecommendEntity, Long> { // <Entity클래스, PK타입>
}
