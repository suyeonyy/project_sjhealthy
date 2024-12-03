package com.example.sjhealthy.dto;

import lombok.Data;

@Data
public class RecommendDTO {
    private Long recId;
    private String memberId;
    private Long recStoreId; // 가게 고유 번호
    private String recStore;
    private String recMenu;
    private String recY; // 좋아요 수
    private String recN; // 싫어요 수
    private Long recViews;
    private String isDeleted;
    private String createDate;
    private String updateDate;
    private String createUser;
    private String updateUser;
}
