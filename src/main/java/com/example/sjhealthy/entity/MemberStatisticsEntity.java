package com.example.sjhealthy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member-statistics_table")
public class MemberStatisticsEntity { // 필요한가..?
    @Id
    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String memberId;

    @Column(columnDefinition = "NUMERIC(18,1)", nullable = false)
    private Double memberAchievementPercentage; // 목표 달성도(%)

    @Column(columnDefinition = "NUMERIC(18,1)", nullable = false)
    private Double memberBmi; // BMI 수치

    @Column(columnDefinition = "NUMERIC(18,0)", nullable = false)
    private Integer memberRank; // 순위

    @PrePersist
    public void prePersist(){
        if (memberAchievementPercentage == null){
            this.memberAchievementPercentage = 0.0;
        }

        if (memberBmi == null){
            this.memberBmi = 0.0;
        }

        if (memberRank == null){
            this.memberRank = 0;
        }
    }
}
