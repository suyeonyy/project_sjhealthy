package com.example.sjhealthy.repository;


import com.example.sjhealthy.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findAllByOrderByBoardIdDesc(); // 기존에 쓰던 거
    Page<BoardEntity> findAllByOrderByBoardIdDesc(Pageable pageable); // 페이징 추가한 거
    List<BoardEntity> findTop5ByOrderByCreateDateDesc();
}
