package com.example.sjhealthy.component;

import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.dto.MemberStatisticsDTO;
import com.example.sjhealthy.entity.MemberStatisticsEntity;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class MemberStatisticsMapper {
    public static MemberStatisticsDTO toMemberStatisticsDTO(MemberStatisticsEntity entity){
        MemberStatisticsDTO dto = new MemberStatisticsDTO();

        dto.setMemberId(entity.getMemberId());
        dto.setMemberAchievementPercentage(entity.getMemberAchievementPercentage());
        dto.setMemberBmi(entity.getMemberBmi());
        dto.setMemberRank(entity.getMemberRank());

        return dto;
    }

    public static MemberStatisticsEntity toMemberStatisticsEntity(MemberStatisticsDTO dto){
        MemberStatisticsEntity entity = new MemberStatisticsEntity();

        entity.setMemberId(dto.getMemberId());
        entity.setMemberAchievementPercentage(dto.getMemberAchievementPercentage());
        entity.setMemberBmi(dto.getMemberBmi());
        entity.setMemberRank(dto.getMemberRank());

        return entity;
    }

    public static List<MemberStatisticsDTO> memberStatisticsDTOFromTuple(List<Tuple> tuples){
        List<MemberStatisticsDTO> dtoList = new ArrayList<>();

        for (Tuple tuple : tuples){
            String memberId = tuple.get("memberId", String.class);

            // nativeQuery로 AVG, SUM 등을 이용해 반환한 값은 BigDecimal로 설정된다. 소수점 계산을 정확하게 처리하기 위함.
            // 따라서 추가로 변환하여 받아준다.
            // 근데 내가 쓴 쿼리의 반환값은 퍼센트가 BigDecimal로 나오고 Rank()가 Long 으로 나옴..
            BigDecimal mAP = tuple.get("memberAchievementPercentage", BigDecimal.class);
            Double memberAchievementPercentage = mAP.doubleValue();

            Long memberRankBefore = tuple.get("memberRank", Long.class);
            int memberRank = memberRankBefore.intValue();

            MemberStatisticsDTO dto = new MemberStatisticsDTO(memberId, memberAchievementPercentage, null, memberRank);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static MemberStatisticsDTO statisticsDTOFromTuple(Tuple tuple) {
        String memberId = tuple.get("memberId", String.class);

        // nativeQuery로 AVG, SUM 등을 이용해 반환한 값은 BigDecimal로 설정된다. 소수점 계산을 정확하게 처리하기 위함.
        // 따라서 추가로 변환하여 받아준다.
        // 근데 내가 쓴 쿼리의 반환값은 퍼센트가 BigDecimal로 나오고 Rank()가 Long 으로 나옴..
        BigDecimal mAP = tuple.get("memberAchievementPercentage", BigDecimal.class);
        Double memberAchievementPercentage = mAP.doubleValue();

        Long memberRankBefore = tuple.get("memberRank", Long.class);
        int memberRank = memberRankBefore.intValue();

        BigDecimal bmi = tuple.get("memberBmi", BigDecimal.class);
        Double memberBmi = bmi.doubleValue();

        System.out.println("변환 성공");

        return new MemberStatisticsDTO(memberId, memberAchievementPercentage, memberBmi, memberRank);
    }
}
