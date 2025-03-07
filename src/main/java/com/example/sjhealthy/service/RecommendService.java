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
import com.example.sjhealthy.repository.MemberRepository;
import com.example.sjhealthy.repository.RecommendRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final MemberRepository memberRepository;

    public List<RecommendDTO> getList(){
        List<RecommendEntity> list = recommendRepository.findAllByOrderByRecIdDesc();
        List<RecommendDTO> dtoList = new ArrayList<>(); // dto로 변환하여 저장

        for (RecommendEntity entity : list){
            dtoList.add(RecommendMapper.toRecommendDTO(entity, entity.getMember().getMemberId()));
        }
        return dtoList;
    }

    public Page<RecommendDTO> getListWithPage(int page, int size){ // 페이징
        Pageable pageable = PageRequest.of(page-1, size);
        Page<RecommendEntity> list = recommendRepository.findAllByOrderByRecIdDesc(pageable);
        return RecommendMapper.convertToRecommendDTOPage(list);
    }

    public List<RecommendDTO> getListTop4(){
        List<RecommendEntity> list = recommendRepository.findTop4();
        List<RecommendDTO> dtoList = new ArrayList<>();

        for (RecommendEntity r : list){
            dtoList.add(RecommendMapper.toRecommendDTO(r, r.getMember().getMemberId()));
        }
        return dtoList;
    }

    public RecommendDTO addRecommendation(RecommendDTO recommendDTO){
        Optional<MemberEntity> entity = memberRepository.findByMemberId(recommendDTO.getMemberId());

        if (entity.isPresent()){
            MemberEntity memberEntity = entity.get();
            //dto를 entity로 변환
            RecommendEntity rEntity = RecommendMapper.toRecommendEntity(recommendDTO, memberEntity);

            //저장
            RecommendEntity savedEntity = recommendRepository.save(rEntity);

            //다시 dto로 변환하여 반환
            return RecommendMapper.toRecommendDTO(savedEntity, savedEntity.getMember().getMemberId());
        } else return null;
    }

    public RecommendEntity readRecommendationById(Long recId){
        Optional<RecommendEntity> entity = recommendRepository.findById(recId);

        RecommendEntity byRecId = null;
        if (entity.isPresent()){
            byRecId = entity.get();
            return byRecId;
        }
        return null;
    }

    public void countRecViews(Long recId){
        Optional<RecommendEntity> entity = recommendRepository.findById(recId);

        RecommendEntity result = null;

        if (entity.isPresent()){
            result = entity.get();
            result.setRecViews(result.getRecViews() + 1);
            recommendRepository.save(result);
        }
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
                recommendEntity.setRecY(recommendEntity.getRecY() + memberId + "_");
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
                recommendEntity.setRecN(recommendEntity.getRecN() + memberId + "_");
            } else {
                // 싫어요를 이미 눌렀을 때
                return false;
            }
        }

        // 수정한 데이터를 DB에 업로드
        recommendRepository.save(recommendEntity);
        return true;
    }

    public List<Tuple> countLikeAndDislike(Long recId){
        // 출력용 좋아요, 싫어요 개수 구하기
        return recommendRepository.getLikeDislikeCount(recId);
    }

    // 지도에서 주변 가게 정보 띄울 때 추천 게시판에 존재하는 정보는 같이 띄우려고, 가게 이름별로 정렬
    public List<RecommendEntity> getListByStoreIdOrPlaceName(String recStoreId, String recStore) {
        return recommendRepository.getRecommendationByStoreNameOrStoreId(recStoreId, recStore);
    }

    public List<RecommendEntity> getListByPlaceName(String recStore){
        return recommendRepository.getRecommendationByStoreName(recStore);
    }

    // 페이지네이션 추가한 검색 결과
    public Page<RecommendDTO> getListByPlaceNameWithPage(String recStore, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "recId"));
        return RecommendMapper.convertToRecommendDTOPage(
            recommendRepository.getRecommendationByStoreNameWithPage(recStore, pageable));
    }

    public RecommendEntity checkByRecStoreAndRecMenu(String recStore, String recMenu){
        return recommendRepository.findByRecStoreAndRecMenu(recStore, recMenu);
    }

    public boolean delete(Long recId){
        Optional<RecommendEntity> rec = recommendRepository.findById(recId);

        if (rec.isPresent()){
            recommendRepository.deleteById(recId);
            return true;
        }
        return false;
    }
}
