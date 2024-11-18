package com.example.sjhealthy.repository;


import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

//    Optional<BoardEntity> save(BoardDTO boardDTO);
// save는 이미 jpa에 있다

}
