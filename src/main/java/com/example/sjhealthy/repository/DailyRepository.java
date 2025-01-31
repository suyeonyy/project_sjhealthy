package com.example.sjhealthy.repository;

import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.dto.MemberStatisticsDTO;
import com.example.sjhealthy.entity.DailyEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.entity.MemberStatisticsEntity;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DailyRepository extends JpaRepository<DailyEntity, Long> {

    // 나중에 기록한 날짜별로 다 저장 되도록 일지 write 메서드 수정 필요
    @Query(value = "SELECT " +
            "d.daily_cur_wt " +
            "FROM daily_table d " +
            "WHERE d.member_id = :memberId AND d.daily_id = " +
            "   (SELECT MAX(d2.daily_id) " +
            "   FROM daily_table d2 " +
            "   WHERE d2.member_id = :memberId)",
            nativeQuery = true)
    Double getMemberWeight(@Param("memberId")String memberId);

    @Query(value = "SELECT " +
        "d.daily_cur_wt " +
        "FROM daily_table d " +
        "WHERE d.member_id = :memberId",
        nativeQuery = true)
    List<Double> getAllWeightByMemberId(@Param("memberId")String memberId);

//    @Query(value = "SELECT " + // 목표 달성도 계산
//            "((d.dailyGoalWt - d.dailyCurWt) / d.dailyGoalWt) * 100 " +
//            "FROM DailyEntity d " +
//            "WHERE d.memberId = :memberId", nativeQuery = true)
//    Double getAchievementPercentage(@Param("memberId") String memberId);// 목표 달성도

//    @Query(value = "SELECT " + // 특정 회원 순위 계산
//            "RANK() OVER (ORDER BY ((d.dailyGoalWt - d.dailyCurWt) / d.dailyGoalWt) * 100 DESC) AS memberRank " + // 순위
//            "FROM DailyEntity d "+
//            "WHERE d.memberId = :memberId", nativeQuery = true) // RANK() 쓰려면
//    Integer getRankByMemberId(@Param("memberId") String memberId);

//    @Query(value = "SELECT d.dailyCurWt " +
//            "FROM DailyEntity d " +
//            "WHERE d.memberId = :memberId", nativeQuery = true)
//    List<Double> getWeightListByMemberId(@Param("memberId") String memberId);


    @Query(value = "SELECT * FROM ( " +
            "SELECT " + // 회원들의 아이디, 목표 달성도를 순위와 함께 가져옴(광장 순위용, 3위까지만)
                "m.member_id AS memberId, " + // memberId
                "((first.daily_cur_wt - d.daily_cur_wt) / (first.daily_cur_wt - d.daily_goal_wt)) * 100 AS memberAchievementPercentage, " +  // 목표 달성도
                "DENSE_RANK() OVER (ORDER BY ((first.daily_cur_wt - d.daily_cur_wt) / (first.daily_cur_wt - d.daily_goal_wt)) DESC) " +
                "AS memberRank " + // 순위
                "FROM daily_table d " + // nativeQuery를 쓰면 테이블로 표기
                "JOIN member_table m ON m.member_id = d.member_id " + // member_table과 조인
                "LEFT JOIN " + // 셀프조인으로 해당 회원의 처음 체중을 가져와 계산에 사용
                "   daily_table first ON first.member_id = m.member_id AND first.daily_id = ( " +
                "       SELECT MIN(daily_id) FROM daily_table WHERE member_id = m.member_id) " +
                "WHERE d.daily_id = (SELECT " + // 현재 체중은 멤버별 가장 최근 데이터 사용
                "                       MAX(d2.daily_id) FROM daily_table d2 WHERE d2.member_id = m.member_id) " +
            ") AS ranked_data " +
            "WHERE memberRank <= 3 " +  // 3위까지만
            "ORDER BY memberRank ASC", nativeQuery = true) // 내림차순으로 조회
    List<Tuple> getRankList();

    @Query(value= "SELECT " + // 특정 멤버의 목표 달성도, 순위, BMI를 구해 가져옴(해당 회원 통계 출력용)
            "m.member_id AS memberId, " + // memberId
            "((first.daily_cur_wt - d.daily_cur_wt) / (first.daily_cur_wt - d.daily_goal_wt)) * 100 AS memberAchievementPercentage, " +  // 목표 달성도
            "ranked.memberRank AS memberRank, " + // 순위
            "(10000 * d.daily_cur_wt / (m.member_height * m.member_height)) AS memberBmi " + // BMI
            "FROM daily_table d " + // nativeQuery를 쓰면 테이블로 표기
            "JOIN member_table m ON m.member_id = d.member_id " + // member_table과 조인
            "LEFT JOIN " + // 셀프조인으로 해당 회원의 처음 체중을 가져와 계산에 사용
            "   daily_table first ON first.member_id = m.member_id AND first.daily_id = ( " +
            "       SELECT MIN(daily_id) FROM daily_table WHERE member_id = :memberId) " +
            "JOIN ( " + // 순위 계산
            "       SELECT " +
            "           d.member_id AS memberId, " +
            "           DENSE_RANK() OVER (ORDER BY ((first.daily_cur_wt - d.daily_cur_wt) / (first.daily_cur_wt - d.daily_goal_wt)) DESC) AS memberRank " +
            "       FROM daily_table d " +
            "       JOIN member_table m ON m.member_id = d.member_id " +
            "       LEFT JOIN daily_table first ON first.member_id = m.member_id AND first.daily_id = ( " +
            "           SELECT MIN(daily_id) FROM daily_table WHERE member_id = m.member_id) " +
            "       WHERE d.daily_id = (SELECT MAX(d2.daily_id) FROM daily_table d2 WHERE d2.member_id = d.member_id) " +
            ") ranked ON ranked.memberId = d.member_id " +
            "WHERE d.member_id = :memberId AND d.daily_id = (SELECT " + // 현재 체중은 멤버별 가장 최근 데이터 사용
            "                       MAX(d2.daily_id) FROM daily_table d2 WHERE d2.member_id = :memberId)"
            , nativeQuery = true)
    Tuple getStatisticsByMemberId(@Param("memberId") String memberId);

    // 그래프용 해당 연,월의 날짜별 체중을 조회
    @Query(value= "SELECT " +
            "d.daily_date AS date, " +
            "d.daily_cur_wt AS weight " +
            "FROM daily_table d " +
            "WHERE d.daily_month = :month AND d.daily_year = :year AND d.member_id = :memberId " +
            "ORDER BY d.daily_date ASC", nativeQuery = true)
    List<Tuple> getWeightByMemberIdAndMonth(@Param("memberId")String memberId, @Param("month")int month, @Param("year")int year);

    //현재 날짜(yyyymmdd)를 기준으로, 현재 월로 등록된 데이터 유무 가져오기
    @Query(value = "SELECT  DTBL.DAILY_DATE " +
            ",DTBL.DAILY_TITLE " +
            ",DTBL.DAILY_CUR_WT " +
            "FROM  daily_table DTBL " +
            "WHERE  1 = 1 " +
            "AND  DTBL.MEMBER_ID = :loginId " +
            "AND  DTBL.DAILY_YEAR = :year " +
            "AND  DTBL.DAILY_MONTH = :month " +
            "ORDER BY DTBL.DAILY_DATE",
            nativeQuery = true)
    List<Object[]> getDateList(@Param("loginId") String loginId, @Param("year") String year, @Param("month") String month);

    //일지 상세내역
    @Query(value = "SELECT  dtbl.daily_id, " +
                           "dtbl.daily_title, " +
                           "dtbl.member_id, " +
                           "dtbl.daily_date, " +
                           "dtbl.daily_cur_wt, " +
                           "dtbl.daily_goal_wt, " +
                           "dtbl.daily_goal_sf, " +
                           "dtbl.daily_memo, " +
                           "dtbl.daily_month, " +
                           "dtbl.daily_year, " +
                           "dtbl.is_deleted, " +
                           "dtbl.create_date, " +
                           "dtbl.update_date, " +
                           "dtbl.create_user, " +
                           "dtbl.update_user " +
                     "FROM  daily_table dtbl " +
                    "WHERE  1 = 1 " +
                      "AND  dtbl.member_id = :loginId " +
                      "AND  dtbl.daily_date = :dailyDate ",
            nativeQuery = true)
    Optional<DailyEntity> getDateView(@Param("loginId") String loginId, @Param("dailyDate") String dailyDate);







}
