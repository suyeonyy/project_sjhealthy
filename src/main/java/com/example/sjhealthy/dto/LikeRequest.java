package com.example.sjhealthy.dto;

import lombok.Data;

@Data
public class LikeRequest {
    // 뷰에서 보낸 좋아요 싫어요를 받기 위한 클래스
    private Long recY;
    private Long recN;
    private String memberId;
    private String recId;
}
