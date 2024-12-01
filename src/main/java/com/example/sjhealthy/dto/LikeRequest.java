package com.example.sjhealthy.dto;

import lombok.Data;

@Data
public class LikeRequest {
    // 뷰에서 보낸 좋아요 싫어요를 받기 위한 클래스
    private String action; // "like" 와 "dislike"로 받음
    private Long recId;

}
