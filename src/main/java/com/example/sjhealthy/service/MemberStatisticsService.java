package com.example.sjhealthy.service;

import com.example.sjhealthy.component.MemberStatisticsMapper;
import com.example.sjhealthy.dto.MemberStatisticsDTO;
import com.example.sjhealthy.entity.DailyEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.entity.MemberStatisticsEntity;
import com.example.sjhealthy.repository.DailyRepository;
import com.example.sjhealthy.repository.MemberStatisticsRepository;
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

    public Double getAchievementPercentage(String memberId){
        return dailyRepository.getAchievementPercentage(memberId);
    }

    public Double getBMI(String memberId){
        return dailyRepository.getBMI(memberId);
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

    public MemberStatisticsDTO getTotalStatisticsByMemberId(String memberId){
        MemberStatisticsEntity byMemberId = dailyRepository.getTotalStatisticsByMemberId(memberId);

        memberStatisticsRepository.save(byMemberId);

        return MemberStatisticsMapper.toMemberStatisticsDTO(byMemberId);
    }
}
