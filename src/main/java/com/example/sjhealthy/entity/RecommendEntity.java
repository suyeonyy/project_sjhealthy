package com.example.sjhealthy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@Table(name = "recommend_table")
public class RecommendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recId;

//    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
//    private String memberId;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String recStoreId;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String recStore;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String recStoreGroupCode;

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String recMenu;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String recY;

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String recN;

    @Column(columnDefinition = "NUMERIC(18,0) DEFAULT 0", nullable = false)
    private Long recViews;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // 지연
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;


    @PrePersist
    public void prePersist(){
        if (recY == null){ this.recY = ""; }
        if (recN == null){ this.recN = ""; }

        if (this.createDate == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            this.createDate = LocalDate.now().format(formatter);
        }

        if (this.updateDate == null) this.updateDate = this.createDate;

        if (this.createUser == null){
            this.createUser = this.member.getMemberId() != null ? this.member.getMemberId() : "defaultUser";
            this.updateUser = this.member.getMemberId() != null ? this.member.getMemberId() : "defaultUser";
        } else if (!this.createUser.equals(this.member.getMemberId())){
            this.updateUser = this.member.getMemberId() != null ? this.member.getMemberId() : "defaultUser";
        }

        if (this.isDeleted == null){
            this.isDeleted = "N";
        }

        if (this.recViews == null){
            this.recViews = 0L;
        }
    }

    @PreUpdate
    public void preUpdate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.updateDate = LocalDate.now().format(formatter);
    }
}
