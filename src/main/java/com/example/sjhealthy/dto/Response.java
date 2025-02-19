package com.example.sjhealthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> { // data 필드를 제네릭으로 받아서 어떠한 타입의 데이터도 받을 수 있다.
    private T data;
    private String message;
    private Long likeCount;
    private Long dislikeCount;
    private T data2; // 추천게시판에서 관리자 확인 때만 쓰려고

    public Response(T data, String message){ // 보통 사용. 4개 다 쓰는 건 Rec 게시판 상세페이지에서만
        this(data, message, 0L, 0L, null);
    }

    public Response(T data, String message, Long likeCount, Long dislikeCount){
        this(data, message, likeCount, dislikeCount, null);
    }
    public Response(T data, T data2, String message){ // 관리자 확인 때만 쓸 것임
        this(data, message, 0L, 0L, data2);
    }

}
