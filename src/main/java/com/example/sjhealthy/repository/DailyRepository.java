/*
package com.example.sjhealthy.repository;

import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.entity.DailyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyRepository extends JpaRepository<DailyEntity, Long> {
    @Query("SELECT " + // 목표 달성도 계산
                "((:goal - :weight) / :goal) * 100 " +
                "FROM DailyEntity " +
                "WHERE memberId = :memberId")
    DailyEntity getAchievementPercentage(Long goal, Float weight, String memberId);// 목표 달성도

    @Query("SELECT " + // BMI 수치 계산
                "(703 * :weight / (:height * :height)) AS BMI " +
                "FROM DailyEntity " +
                "WHERE memberId = :memberId")
    Float getBMI(Float weight, Long height, String memberId); // BMI 계산

    @Query("SELECT weight " +
                "FROM DailyEntity " +
                "WHERE memberId = :memberId")
    List<Float> getWeightListByMemberId(String memberId);
}

 */
