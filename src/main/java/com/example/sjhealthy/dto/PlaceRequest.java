package com.example.sjhealthy.dto;

import lombok.Data;

@Data
public class PlaceRequest {
    private String storeId; // 가게 고유번호, 기본은 고유번호로 연결
    private String placeName; // 프랜차이즈에 적용하기 위해 이름도 받음
}
