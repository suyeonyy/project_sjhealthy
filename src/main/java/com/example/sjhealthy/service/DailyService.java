package com.example.sjhealthy.service;

import com.example.sjhealthy.component.BoardMapper;
import com.example.sjhealthy.component.DailyMapper;
import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.DailyDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.DailyEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.repository.DailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final DailyRepository dailyRepository;
    private final MemberService memberService;

    public DailyDTO write(DailyDTO dailyDTO, MemberEntity memberEntity) {
        //DTO -> Entity
        DailyEntity postEntity = DailyMapper.toDailyEntity(dailyDTO, memberEntity);
        //저장
        DailyEntity saveEntity = dailyRepository.save(postEntity);
        //다시 DTO로 변환하여 반환
        return DailyMapper.toDailyDTO(saveEntity, memberEntity.getMemberId());
    }

    public List<DailyDTO> getList(String loginId) {
        MemberDTO dto = memberService.findMemberIdAtPassFind(loginId);

        List<DailyEntity> dailyList = dailyRepository.findAll();
        List<DailyDTO> list = new ArrayList<>();
        for(DailyEntity post : dailyList){
            if(post.getMember().getMemberId().equals(loginId) || dto.getMemberAuth().equals("A")){
                list.add(DailyMapper.toDailyDTO(post, post.getMember().getMemberId()));
            }
        }
        return list;
    }

    public DailyDTO read(Long dailyId, MemberEntity memberEntity) {
        Optional<DailyEntity> entity = dailyRepository.findById(dailyId);

        if(entity.isPresent()){
            DailyEntity readEntity = entity.get();
            return DailyMapper.toDailyDTO(readEntity, memberEntity.getMemberId());
        }else{
            return null;
        }
    }
}
