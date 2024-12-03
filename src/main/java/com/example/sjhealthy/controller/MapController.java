package com.example.sjhealthy.controller;

import com.example.sjhealthy.dto.PlaceRequest;
import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.entity.RecommendEntity;
import com.example.sjhealthy.service.MapService;
import com.example.sjhealthy.service.RecommendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    private final MapService mapService;
    private final RecommendService recommendService;


    @GetMapping("/map")
    public String getMapForm(Model model){
        List<RecommendDTO> recommendList = recommendService.getList();
        model.addAttribute("recommendDTO", recommendList);

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

    @ResponseBody
    @PostMapping("/map/getrecommend")
    public ResponseEntity<Set<RecommendEntity>> getRecommendList(@RequestBody PlaceRequest placeRequest){

        Long storeId = placeRequest.getStoreId();
        String placeName = placeRequest.getPlaceName();

        // 두 가지 조건으로 가져오기 때문에 중복을 없애기 위해 List 말고 Set 사용
        Set<RecommendEntity> recommendDTOList = recommendService.getListByStoreIdOrPlaceName(storeId, placeName);
        return ResponseEntity.ok(recommendDTOList);

    }

}
