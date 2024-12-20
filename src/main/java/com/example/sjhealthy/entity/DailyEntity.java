package com.example.sjhealthy.entity;

import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.service.DailyService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@Table(name = "daily_table")
public class DailyEntity { // 일지

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyId;

//    private String memberId;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String dailyDate;

    @Column(columnDefinition = "NUMERIC(18,2) DEFAULT 0.00", nullable = false)
    private double dailyCurWt;

    @Column(columnDefinition = "NUMERIC(18,2) DEFAULT 0.00", nullable = false)
    private double dailyGoalWt;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String dailyGoalSf;

    @Column(columnDefinition = "VARCHAR(2000)", nullable = false)
    private String dailyMemo;

    @Column(columnDefinition = "VARCHAR(4)", nullable = false)
    private String dailyYear;

    @Column(columnDefinition = "VARCHAR(2)", nullable = false)
    private String dailyMonth;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT 'N'", nullable = false)
    private String isDeleted;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String createDate;

    @Column(columnDefinition = "VARCHAR(8)", nullable = false)
    private String updateDate;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String createUser;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String updateUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private MemberEntity member; // 참조하여 memberHeight 를 사용

    @PrePersist
    public void prePersist() {
        /*등록일, 수정일*/
        if (this.createDate == null) {
            // 날짜 저장 형식 지정하여 현재시간 저장
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            this.createDate = LocalDate.now().format(formatter);
        }

        if (this.updateDate == null) {
            //등록일과 동일하게 저장
            this.updateDate = this.createDate;
        }

        /*등록자, 수정자*/
        if (this.createUser == null) {
            this.createUser = this.member.getMemberId() != null ? this.member.getMemberId() : "defaultUser";
        }

        //수정자는 무조건 업데이트
        this.createUser = this.member.getMemberId() != null ? this.member.getMemberId() : "defaultUser";
        /*
        if (this.updateUser == null) {
            this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
        }
        */

        /*삭제여부*/
        if(this.isDeleted == null){
            this.isDeleted = "N";
        }

    }
}
