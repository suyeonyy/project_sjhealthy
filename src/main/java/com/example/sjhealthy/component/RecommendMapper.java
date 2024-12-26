package com.example.sjhealthy.component;

import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.entity.RecommendEntity;
import lombok.Data;

@Data
public class RecommendMapper {

    public static RecommendDTO toRecommendDTO(RecommendEntity recommendEntity, String memberId){
        RecommendDTO recommendDTO = new RecommendDTO();

        recommendDTO.setRecId(recommendEntity.getRecId());
//        recommendDTO.setMemberId(recommendEntity.getMemberId());
        recommendDTO.setRecStoreId(recommendEntity.getRecStoreId());
        recommendDTO.setRecStore(recommendEntity.getRecStore());
        recommendDTO.setRecStoreGroupCode(recommendEntity.getRecStoreGroupCode());
        recommendDTO.setRecMenu(recommendEntity.getRecMenu());
        recommendDTO.setRecY(recommendEntity.getRecY());
        recommendDTO.setRecN(recommendEntity.getRecN());
        recommendDTO.setIsDeleted(recommendEntity.getIsDeleted());
        recommendDTO.setRecViews(recommendEntity.getRecViews());
        recommendDTO.setCreateDate(recommendEntity.getCreateDate());
        recommendDTO.setUpdateDate(recommendEntity.getUpdateDate());
        recommendDTO.setCreateUser(recommendEntity.getCreateUser());
        recommendDTO.setUpdateUser(recommendEntity.getUpdateUser());
        recommendDTO.setMemberId(memberId);

        return recommendDTO;
    }

    public static RecommendEntity toRecommendEntity(RecommendDTO recommendDTO, MemberEntity memberEntity){
        RecommendEntity recommendEntity = new RecommendEntity();

        recommendEntity.setRecId(recommendDTO.getRecId());
//        recommendEntity.setMemberId(recommendDTO.getMemberId());
        recommendEntity.setRecStoreId(recommendDTO.getRecStoreId());
        recommendEntity.setRecStore(recommendDTO.getRecStore());
        recommendEntity.setRecStoreGroupCode(recommendDTO.getRecStoreGroupCode());
        recommendEntity.setRecMenu(recommendDTO.getRecMenu());
        recommendEntity.setRecY(recommendDTO.getRecY());
        recommendEntity.setRecN(recommendDTO.getRecN());
        recommendEntity.setIsDeleted(recommendDTO.getIsDeleted());
        recommendEntity.setRecViews(recommendDTO.getRecViews());
        recommendEntity.setCreateDate(recommendDTO.getCreateDate());
        recommendEntity.setUpdateDate(recommendDTO.getUpdateDate());
        recommendEntity.setCreateUser(recommendDTO.getCreateUser());
        recommendEntity.setUpdateUser(recommendDTO.getUpdateUser());
        recommendEntity.setMember(memberEntity);

        return recommendEntity;
    }
}
