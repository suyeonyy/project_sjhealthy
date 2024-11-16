package com.example.sjhealthy.service;

import com.example.sjhealthy.component.BoardMapper;
import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardDTO write(BoardDTO boardDTO){
        // dto를 entity로 변환
        BoardEntity postEntity = BoardMapper.toBoardEntity(boardDTO);
        // 저장
        BoardEntity savedEntity = boardRepository.save(postEntity);
        // 다시 dto로 변환해 반환
        return BoardMapper.toBoardDTO(savedEntity);
    }
}
