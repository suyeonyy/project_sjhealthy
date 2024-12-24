package com.example.sjhealthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage { // 좋아요 싫어요
    private String message;
    private Long likeCount;
    private Long dislikeCount;

    public ResponseMessage(String message){
        this(message, null, null);
    }

}
