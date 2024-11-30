package com.example.sjhealthy.service;

import com.example.sjhealthy.component.RecommendMapper;
import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.entity.RecommendEntity;
import com.example.sjhealthy.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public RecommendDTO addRecommendation(RecommendDTO recommendDTO){
        RecommendEntity entity = recommendRepository.save(RecommendMapper.toRecommendEntity(recommendDTO));

        return RecommendMapper.toRecommendDTO(entity);
    }

    public RecommendEntity readRecommendationById(Long recId){
        Optional<RecommendEntity> entity = recommendRepository.findById(recId);

        RecommendEntity byRecId;
        if (entity.isPresent()){
            byRecId = entity.get();
            return byRecId;
        }
        return null;
    }
}
