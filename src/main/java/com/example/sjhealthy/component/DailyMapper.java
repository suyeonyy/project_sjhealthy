package com.example.sjhealthy.component;

import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.DailyEntity;
import com.example.sjhealthy.entity.MemberEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class DailyMapper { // 일지
    public static DailyEntity toDailyEntity(DailyDTO dailyDTO, MemberEntity memberEntity){
        DailyEntity dailyEntity = new DailyEntity();

        dailyEntity.setDailyId(dailyDTO.getDailyId());
//        dailyEntity.setMemberId(dailyDTO.getMemberId());
        dailyEntity.setDailyTitle(dailyDTO.getDailyTitle());
        dailyEntity.setDailyDate(dailyDTO.getDailyDate());
        dailyEntity.setDailyCurWt(dailyDTO.getDailyCurWt());
        dailyEntity.setDailyGoalWt(dailyDTO.getDailyGoalWt());
        dailyEntity.setDailyGoalSf(dailyDTO.getDailyGoalSf());
        dailyEntity.setDailyMemo(dailyDTO.getDailyMemo());
        dailyEntity.setDailyYear(dailyDTO.getDailyYear());
        dailyEntity.setDailyMonth(dailyDTO.getDailyMonth());
        dailyEntity.setIsDeleted(dailyDTO.getIsDeleted());
        dailyEntity.setCreateDate(dailyDTO.getCreateDate());
        dailyEntity.setUpdateDate(dailyDTO.getUpdateDate());
        dailyEntity.setCreateUser(dailyDTO.getCreateUser());
        dailyEntity.setUpdateUser(dailyDTO.getUpdateUser());
        dailyEntity.setMember(memberEntity);

        return dailyEntity;
    }

    public static DailyDTO toDailyDTO(DailyEntity dailyEntity, String memberId){
        DailyDTO dailyDTO = new DailyDTO();

        dailyDTO.setDailyId(dailyEntity.getDailyId());
//        dailyDTO.setMemberId(dailyEntity.getMemberId());
        dailyDTO.setDailyTitle(dailyEntity.getDailyTitle());
        dailyDTO.setDailyDate(dailyEntity.getDailyDate());
        dailyDTO.setDailyCurWt(dailyEntity.getDailyCurWt());
        dailyDTO.setDailyGoalWt(dailyEntity.getDailyGoalWt());
        dailyDTO.setDailyGoalSf(dailyEntity.getDailyGoalSf());
        dailyDTO.setDailyMemo(dailyEntity.getDailyMemo());
        dailyDTO.setDailyYear(dailyEntity.getDailyYear());
        dailyDTO.setDailyMonth(dailyEntity.getDailyMonth());
        dailyDTO.setIsDeleted(dailyEntity.getIsDeleted());
        dailyDTO.setCreateDate(dailyEntity.getCreateDate());
        dailyDTO.setUpdateDate(dailyEntity.getUpdateDate());
        dailyDTO.setCreateUser(dailyEntity.getCreateUser());
        dailyDTO.setUpdateUser(dailyEntity.getUpdateUser());
        dailyDTO.setMemberId(memberId); // member 객체 중 memberId만 필요해서 이것만 받아서 DTO로 보냄

        return dailyDTO;
    }
}
