package com.example.sjhealthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //@Data: @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 를 합쳐놓은
@NoArgsConstructor
@AllArgsConstructor
public class DailyDTO { // 일지
    private Long dailyId;       //글 고유번호
//    private String memberId;    //아이디
    private MemberDTO member;
    private String dailyDate;   //작성일
    private double dailyCurWt;     //현재 체중
    private double dailyGoalWt;    //목표 체중
    private String dailyGoalSf; //목표 만족도
    private String dailyMemo;   //메모
    private String dailyYear;   //년도
    private String dailyMonth;  //월
    private String isDeleted;   //삭제여부
    private String createDate;  //등록일
    private String updateDate;  //수정일
    private String createUser;  //등록자
    private String updateUser;  //수정자
}
