package com.example.sjhealthy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String memberId;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String recStore;

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String recMenu;

    @Column(columnDefinition = "NUMERIC(18,0) DEFAULT 0", nullable = false)
    private Long recY;

    @Column(columnDefinition = "NUMERIC(18,0) DEFAULT 0", nullable = false)
    private Long recN;

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

    @PrePersist
    public void prePersist(){
        if (recY == null){ this.recY = 0L; }
        if (recN == null){ this.recN = 0L; }

        if (this.createDate == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
            this.createDate = LocalDate.now().format(formatter);
        }

        if (this.updateDate == null) this.updateDate = this.createDate;

        if (this.createUser == null){
            this.createUser = this.memberId != null ? this.memberId : "defaultUser";
            this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
        }

        if (!this.createUser.equals(this.memberId)){
            this.updateUser = this.memberId != null ? this.memberId : "defaultUser";
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
