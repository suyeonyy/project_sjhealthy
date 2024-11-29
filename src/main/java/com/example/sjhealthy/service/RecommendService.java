package com.example.sjhealthy.service;

import com.example.sjhealthy.component.RecommendMapper;
import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.entity.RecommendEntity;
import com.example.sjhealthy.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendRepository recommendRepository;

    public List<RecommendDTO> getList(){
        List<RecommendEntity> list = recommendRepository.findAll();
        List<RecommendDTO> dtoList = new ArrayList<>(); // dto로 변환하여 저장

        for (RecommendEntity entity : list){
            dtoList.add(RecommendMapper.toRecommendDTO(entity));
        }
        return dtoList;
    }

}
