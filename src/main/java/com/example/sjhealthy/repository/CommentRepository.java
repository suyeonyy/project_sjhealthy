package com.example.sjhealthy.repository;

import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    //메서드 정의
    // select * from comment_table where 1 = 1  and board_id=? order by id desc;
    List<CommentEntity> findAllByBoardEntityOrderByComIdDesc(BoardEntity boardEntity);

    // 특정 게시판에 대한 가장 큰 commentOrder를 찾아 반환 (가장 마지막 댓글의 순서)
    @Query("SELECT MAX(c.commentOrder) FROM CommentEntity c WHERE c.boardEntity.id = :boardId")
    Long findMaxCommentOrderByBoardId(Long boardId);
}
