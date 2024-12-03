package com.example.sjhealthy.service;

import lombok.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MapService {
    /**
     * 좌표 기준 맵 데이터 조회
     * 파라미터 순서는 x, y 순서로 넘겨줘야 데이터 리턴 받을 수 있음
     * @param longitude 경도 -> x
     * @param latitude 위도 -> y
     * @param page 페이지 수 1,2,3
     * @param size 페이지당 조회 수 15
     * @return ResponseEntity
     */
    /*
    public ResponseEntity<String> getSearchMapList(String longitude, String latitude, String page, String size) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); //포맷 JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK fccc633fbf224f6e9fd25528eee2a160");

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String baseUrl = "https://dapi.kakao.com/v2/local/search/category.json?" +
                         "category_group_code=FD6" + //음식점코드 FD6, 카페코드 CE7
                         "&page" + page +
                         "&size" + size +
                         "&sort=accuracy" +
                         "&x=" + longitude +
                         "&y=" + latitude;

        RestTemplate restTemplate = new RestTemplate();

        //exchange 메서드로 템플릿 전달
        return restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);
    }
     */
}
