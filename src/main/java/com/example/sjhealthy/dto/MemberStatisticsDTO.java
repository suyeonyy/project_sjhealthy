package com.example.sjhealthy.dto;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatisticsDTO {
    private String memberId;
    private Double memberAchievementPercentage; // 목표 달성도(%)
    private Double memberBmi; // BMI 수치
    private int memberRank; // 순위

}
