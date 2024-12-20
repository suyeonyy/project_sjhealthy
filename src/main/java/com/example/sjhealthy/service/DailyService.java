package com.example.sjhealthy.service;

import com.example.sjhealthy.component.BoardMapper;
import com.example.sjhealthy.component.DailyMapper;
import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.DailyEntity;
import com.example.sjhealthy.repository.DailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final DailyRepository dailyRepository;


    public DailyDTO write(DailyDTO dailyDTO) {
        //DTO -> Entity
        DailyEntity postEntity = DailyMapper.toDailyEntity(dailyDTO);
        //저장
        DailyEntity saveEntity = dailyRepository.save(postEntity);
        //다시 DTO로 변환하여 반환
        return DailyMapper.toDailyDTO(saveEntity);
    }

    public List<DailyDTO> getList() {
        List<DailyEntity> dailyList = dailyRepository.findAll();
        List<DailyDTO> list = new ArrayList<>();
        for(DailyEntity post : dailyList){
            list.add(DailyMapper.toDailyDTO(post));
        }
        return list;
    }
}
