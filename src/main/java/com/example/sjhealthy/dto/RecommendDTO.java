package com.example.sjhealthy.dto;

import lombok.Data;

@Data
public class RecommendDTO {
    private Long recId;
    private String memberId;
    private String recStore;
    private String recMenu;
    private Long recY;
    private Long recN;
    private Long recViews;
    private String isDeleted;
    private String createDate;
    private String updateDate;
    private String createUser;
    private String updateUser;
}
