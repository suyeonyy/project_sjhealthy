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
import com.example.sjhealthy.repository.MemberRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final DailyRepository dailyRepository;
    private final MemberRepository memberRepository;

    public DailyDTO write(DailyDTO dailyDTO) {
        Optional<MemberEntity> entity = memberRepository.findByMemberId(dailyDTO.getMemberId());

        if (entity.isPresent()){
            MemberEntity memberEntity = entity.get();
            //DTO -> Entity
            DailyEntity postEntity = DailyMapper.toDailyEntity(dailyDTO, memberEntity);
            //저장
            DailyEntity saveEntity = dailyRepository.save(postEntity);
            //다시 DTO로 변환하여 반환
            return DailyMapper.toDailyDTO(saveEntity, memberEntity.getMemberId());
        } else return null;
    }

    public List<DailyDTO> getList(String loginId) {
        List<DailyEntity> dailyList = dailyRepository.findAll();
        List<DailyDTO> list = new ArrayList<>();
        for(DailyEntity post : dailyList){
            if(post.getMember().getMemberId().equals(loginId) || post.getMember().getMemberAuth().equals("A")){
                list.add(DailyMapper.toDailyDTO(post, post.getMember().getMemberId()));
            }
        }
        return list;
    }

    public List<DailyDTO> getDateList(String loginId, String year, String month) {
        // 레포지토리에서 데이터 조회 (List<Object[]> 형태로 반환된다고 가정)
        List<Object[]> queryResults = dailyRepository.getDateList(loginId, year, month);

        // 결과를 DTO 리스트로 변환
        List<DailyDTO> dailyList = new ArrayList<>();

        // 쿼리 결과를 하나씩 순회하여 DTO 객체 생성
        for (Object[] result : queryResults) {
            String dailyDate = (String) result[0]; // 첫 번째 결과는 dailyDate
            String dailyTitle = (String) result[1]; // 두 번째 결과는 dailyTitle

            // DTO 객체 생성
            DailyDTO dailyDTO = new DailyDTO(dailyDate, dailyTitle);

            // DTO 리스트에 추가
            dailyList.add(dailyDTO);
        }

        // 변환된 DTO 리스트 반환
        return dailyList;

    }

    public DailyDTO read(String loginId, String dailyDate) {
        Optional<DailyEntity> entity = dailyRepository.getDateView(loginId, dailyDate);

        if(entity.isPresent()){
            DailyEntity readEntity = entity.get();
            return DailyMapper.toDailyDTO(readEntity, readEntity.getMember().getMemberId());
        }else{
            return null;
        }
    }


    public DailyDTO readView(Long dailyId){
        Optional<DailyEntity> entity = dailyRepository.findById(dailyId);

        if (entity.isPresent()){
            DailyEntity readEntity = entity.get();
            return DailyMapper.toDailyDTO(readEntity, readEntity.getMember().getMemberId());
        } else return null;
    }

    public DailyDTO update(DailyDTO dailyDTO){ // update도 이걸로 사용
        Optional<MemberEntity> entity = memberRepository.findByMemberId(dailyDTO.getMemberId());
        if (entity.isPresent()){
            MemberEntity memberEntity = entity.get();
            DailyEntity postEntity = DailyMapper.toDailyEntity(dailyDTO, memberEntity);
            // 저장
            DailyEntity savedEntity = dailyRepository.save(postEntity);
            // 다시 dto로 변환해 반환
            return DailyMapper.toDailyDTO(savedEntity, savedEntity.getMember().getMemberId());
        } else return null;
    }

}
