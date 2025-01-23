package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.PlaceRequest;
import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.entity.RecommendEntity;
import com.example.sjhealthy.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RequestMapping("/sjhealthy/")
@Controller
@RequiredArgsConstructor
public class MapController {
    //아래 final + RequiredArgsConstructor 적으면 autowird 안적어도됌
    private final RecommendService recommendService;

    @Value("${JS_APPKEY}")
    private String js_appKey;

    @GetMapping("/map")
    public String getMapForm(Model model){
        List<RecommendDTO> recommendList = recommendService.getList();
        model.addAttribute("recommendDTO", recommendList);
        model.addAttribute("js_appKey", js_appKey);

        return "map";
    }

    @ResponseBody
    @PostMapping("/map/getrecommend")
    public ResponseEntity<List<RecommendEntity>> getRecommendList(@RequestBody PlaceRequest placeRequest){

        String storeId = placeRequest.getStoreId();
        String placeName = placeRequest.getPlaceName();
        //System.out.println(placeRequest);

        // Recommend 게시판에서 일정 기준 이상 달성한 것만 리스트로 뽑아 보내는 로직을 추가해야 한다.
        List<RecommendEntity> recommendEntityList = recommendService.getListByStoreIdOrPlaceName(storeId, placeName);
        //System.out.println("recommendEntityList = " + recommendEntityList);
        return ResponseEntity.ok(recommendEntityList);

    }

}
