package com.example.sjhealthy.service;

import com.example.sjhealthy.component.BoardMapper;
import com.example.sjhealthy.component.MemberMapper;
import com.example.sjhealthy.component.RecommendMapper;
import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.entity.RecommendEntity;
import com.example.sjhealthy.repository.RecommendRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        //dro를 entity로 변환
        RecommendEntity entity = RecommendMapper.toRecommendEntity(recommendDTO);

        //저장
        RecommendEntity savedEntity = recommendRepository.save(entity);

        //다시 dto로 변환하여 반환
        return RecommendMapper.toRecommendDTO(savedEntity);
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

    public boolean handleLikeOrDislike(Long recId, String memberId, String type){
        // 처리가 제대로 되었는지 여부를 반환
        Optional<RecommendEntity> recommend = recommendRepository.findById(recId);

        if (recommend.isEmpty()){ //  = (!recommend.isPresent())
            throw new RuntimeException("추천글이 존재하지 않습니다.");
        }

        RecommendEntity recommendEntity = recommend.get();

        // 좋아요를 눌렀을 때
        if ("like".equals(type)){
            // 이전에 싫어요를 누른 상태일 땐 싫어요에서 memberId를 제거
            if (recommendEntity.getRecN().contains(memberId)){
                // 'memberId_'를 지움
                recommendEntity.setRecN(recommendEntity.getRecN().replace(memberId + "_", ""));
            }

            // 좋아요 처리
            if (!recommendEntity.getRecY().contains(memberId)){
                // 좋아요를 누른 적 없을 때
                recommendEntity.setRecY(memberId + "_");
            } else {
                // 좋아요를 이미 눌렀을 때
                return false;
            }
        } else if ("dislike".equals(type)){
            // 싫어요를 눌렀을 때
            // 이전에 좋아요를 누른 상태일 땐 좋아요에서 memeberId를 제거
            if (recommendEntity.getRecY().contains(memberId)){
                recommendEntity.setRecY(recommendEntity.getRecY().replace(memberId + "_", ""));
            }

            // 싫어요 처리
            if (!recommendEntity.getRecN().contains(memberId)){
                // 싫어요를 누른 적 없을 때
                recommendEntity.setRecN(memberId + "_");
            } else {
                // 싫어요를 이미 눌렀을 때
                return false;
            }
        }

        // 수정한 데이터를 DB에 업로드
        recommendRepository.save(recommendEntity);
        System.out.println("좋/싫 수정 = " + recommendEntity);
        return true;
    }

    public List<Tuple> countLikeAndDislike(Long recId){
        // 출력용 좋아요, 싫어요 개수 구하기
        return recommendRepository.getLikeDislikeCount(recId);
    }
/*
    // 지도에서 주변 가게 정보 띄울 때 추천 게시판에 존재하는 정보는 같이 띄우려고
    public List<RecommendEntity> getListByStoreIdOrPlaceName(String recStoreId, String recStore) {
        return recommendRepository.getRecommendationByStoreNameOrStoreId(recStoreId, recStore);
    }
 */
}
