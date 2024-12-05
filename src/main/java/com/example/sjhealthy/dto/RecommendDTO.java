package com.example.sjhealthy.dto;

import lombok.Data;

@Data
public class RecommendDTO {
    private Long recId;
    private String memberId;
    private String recStoreId; // 업체 고유 번호
    private String recStore; //업체 명
    private String recStoreGroupCode; //업체 그룹 코드
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
