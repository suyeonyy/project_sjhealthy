package com.example.sjhealthy.service;

import com.example.sjhealthy.component.MemberStatisticsMapper;
import com.example.sjhealthy.dto.MemberStatisticsDTO;
import com.example.sjhealthy.entity.DailyEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.entity.MemberStatisticsEntity;
import com.example.sjhealthy.repository.DailyRepository;
import com.example.sjhealthy.repository.MemberStatisticsRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class MemberStatisticsService {

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private MemberStatisticsRepository memberStatisticsRepository;

    // 가장 최근 데이터
    public Double getMemberWeight(String memberId){
        return dailyRepository.getMemberWeight(memberId);
    }

    // 해당 회원의 한 달치 몸무게를 리스트로
//    public List<Map<String, Double>> getWeightListByMemberIdAndMonth(String memberId, int month, int year) {
//        int m, y;
//
//        if (month == 0){ // 0으로 오면 해당 월을 불러와 사용
//            LocalDate currentDate = LocalDate.now();
//            m = currentDate.getMonthValue();
//            System.out.println("month: " + m);
//        } else {
//            // 0이 아니면 해당 월에서 증감하여 사용
//            LocalDate currentDate = LocalDate.now();
//            m = currentDate.getMonthValue() + month;
//            System.out.println("month: " + m);
//        }
//
//        if (year == 0) { // 0으로 오면 해당 연도를 불러와 사용
//            LocalDate currentDate = LocalDate.now();
//            y = currentDate.getYear();
//            System.out.println("year: " + y);
//        } else {
//            // 0이 아니면 해당 연도에서 증감하여 사용
//            LocalDate currentDate = LocalDate.now();
//            y = currentDate.getYear() + year;
//            System.out.println("year: " + y);
//        }
//
//        List<Tuple> result = dailyRepository.getWeightByMemberIdAndMonth(memberId, m, y);
//        List<Map<String, Double>> mapList = new ArrayList<>();
//        for (Tuple r : result){
//            String date = r.get("date", String.class);
//            BigDecimal beforeWeight = r.get("weight", BigDecimal.class);
//            Double weight = null;
//            if (beforeWeight != null){
//                weight = beforeWeight.doubleValue();
//            }
//            Map<String, Double> map = new HashMap<>();
//            map.put(date, weight); // 데이터를 Map으로 만들고
//            mapList.add(map);   // 리스트에 추가
//        }
//
//        System.out.println("Tuple -> Map 변환 완료");
//        return mapList;
//    }
    public List<Map<String, Double>> getWeightListByMemberIdAndMonth(String memberId, int month, int year) {
        List<Tuple> result = dailyRepository.getWeightByMemberIdAndMonth(memberId, month, year);
        List<Map<String, Double>> mapList = new ArrayList<>();
        for (Tuple r : result){
            String date = r.get("date", String.class);
            BigDecimal beforeWeight = r.get("weight", BigDecimal.class);
            Double weight = null;
            if (beforeWeight != null){
                weight = beforeWeight.doubleValue();
            }
            Map<String, Double> map = new HashMap<>();
            map.put(date, weight); // 데이터를 Map으로 만들고
            mapList.add(map);   // 리스트에 추가
        }
        return mapList;
    }

    public List<Map<String, Double>> getWeightListByMemberIdAndYear(String memberId, int year) {
        List<Tuple> result = dailyRepository.getWeightByMemberIdAndYear(memberId, year);
        List<Map<String, Double>> mapList = new ArrayList<>();
        for (Tuple r : result){
            String date = r.get("date", String.class);
            BigDecimal beforeWeight = r.get("weight", BigDecimal.class);
            Double weight = null;
            if (beforeWeight != null){
                weight = beforeWeight.doubleValue();
            }
            Map<String, Double> map = new HashMap<>();
            map.put(date, weight); // 데이터를 Map으로 만들고
            mapList.add(map);   // 리스트에 추가
        }
        return mapList;
    }

    public List<MemberStatisticsDTO> getRankList(){
        List<MemberStatisticsDTO> dtoList = MemberStatisticsMapper.memberStatisticsDTOFromTuple(dailyRepository.getRankList());

        // 소수점 두 자리까지 출력
        for (MemberStatisticsDTO dto : dtoList){
            if (dto.getMemberAchievementPercentage() != null){
                dto.setMemberAchievementPercentage(Math.round(dto.getMemberAchievementPercentage() * 100.00) / 100.00);
            }
        }

        System.out.println("서비스 getRankList");

        return dtoList;
    }

    public MemberStatisticsDTO getStatisticsByMemberId(String memberId){
        Tuple byMemberId = dailyRepository.getStatisticsByMemberId(memberId);
//        memberStatisticsRepository.save(byMemberId);
        if (byMemberId == null){
            return null;
        }

        MemberStatisticsDTO dto = MemberStatisticsMapper.statisticsDTOFromTuple(byMemberId);
        if (dto.getMemberAchievementPercentage() != null){
            dto.setMemberAchievementPercentage(Math.round(dto.getMemberAchievementPercentage() * 100.00) / 100.00);
        } else {
            dto.setMemberAchievementPercentage(0.00);
        }
        if (dto.getMemberBmi() != null){
            dto.setMemberBmi(Math.round(dto.getMemberBmi() * 100.00) / 100.00);
        } else {
            dto.setMemberBmi(0.00);
            System.out.println("bmi = " + dto.getMemberBmi());
        }
        return dto;
    }
}
