package com.example.sjhealthy.repository;

import com.example.sjhealthy.entity.DailyEntity;
import com.example.sjhealthy.entity.MemberStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

// 통계, 광장
public interface MemberStatisticsRepository extends JpaRepository<MemberStatisticsEntity, String> {
}
