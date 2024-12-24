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
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberStatisticsService {

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private MemberStatisticsRepository memberStatisticsRepository;

    public Double getMemberWeight(String memberId){
        return dailyRepository.getMemberWeight(memberId);
    }

    public List<Double> getWeightListByMemberId(String memberId) {return dailyRepository.getAllWeightByMemberId(memberId);}
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

        MemberStatisticsDTO dto = MemberStatisticsMapper.statisticsDTOFromTuple(byMemberId);
        if (dto.getMemberAchievementPercentage() != null){
            dto.setMemberAchievementPercentage(Math.round(dto.getMemberAchievementPercentage() * 100.00) / 100.00);
        }
        if (dto.getMemberBmi() != null){
            dto.setMemberBmi(Math.round(dto.getMemberBmi() * 100.00) / 100.00);
        }
        return dto;
    }
}
