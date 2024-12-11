package com.example.sjhealthy.repository;

import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    //메서드 정의
    // select * from comment_table where 1 = 1  and board_id=? order by id desc;
    List<CommentEntity> findAllByBoardEntityOrderByComIdDesc(BoardEntity boardEntity);
}
