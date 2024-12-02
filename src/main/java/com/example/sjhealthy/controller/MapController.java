package com.example.sjhealthy.controller;

import com.example.sjhealthy.service.MapService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/sjhealthy/")
@Controller
@RequiredArgsConstructor
public class MapController {
    //아래 final + RequiredArgsConstructor 적으면 autowird 안적어도됌
    private final MapService mapService;


    @GetMapping("/map")
    public String getMapForm(){
        return "map";
    }

    /**
     * 추천 List Post 요청
     * @param request 요청 데이터
     * @return ResponseEntity
     */
    @PostMapping("/map")
    private ResponseEntity<String> searchMap(HttpServletRequest request){
        String longitude = request.getParameter("longitude"); //경도
        String latitude = request.getParameter("latitude"); //위도
        String page = request.getParameter("page");

        return mapService.getSearchMapList(longitude, latitude, page, "1");
    }


}
