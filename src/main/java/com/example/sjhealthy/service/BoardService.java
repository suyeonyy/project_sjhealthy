package com.example.sjhealthy.service;

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
@RequestMapping("/board/")
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardDTO write(@ModelAttribute BoardDTO boardDTO){
            Optional<BoardEntity> writeResult = boardRepository.save(boardDTO);

            if (writeResult.isPresent()){
                BoardEntity entity = writeResult.get();
                BoardDTO dto = BoardDTO.toBoardDTO(entity);
                return dto;
            } else {
                return null;
            }
    }
}
