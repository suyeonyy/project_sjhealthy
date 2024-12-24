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
}
