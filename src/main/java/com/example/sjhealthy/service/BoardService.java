package com.example.sjhealthy.service;

import com.example.sjhealthy.component.BoardMapper;
import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardDTO> getList(){
        List<BoardEntity> boardList = boardRepository.findAll();
        List<BoardDTO> list = new ArrayList<>();
        for (BoardEntity post : boardList){
            list.add(BoardMapper.toBoardDTO(post));
        }
        return list;
    }
    public BoardDTO write(BoardDTO boardDTO){ // update도 이걸로 사용
        // dto를 entity로 변환
        BoardEntity postEntity = BoardMapper.toBoardEntity(boardDTO);
        // 저장
        BoardEntity savedEntity = boardRepository.save(postEntity);
        // 다시 dto로 변환해 반환
        return BoardMapper.toBoardDTO(savedEntity);
    }

    public BoardDTO read(Long boardId){
        Optional<BoardEntity> entity = boardRepository.findById(boardId);

        if (entity.isPresent()){
            BoardEntity readEntity = entity.get();
            return BoardMapper.toBoardDTO(readEntity);
        } else return null;
    }

    public boolean delete(Long boardId){
        if (boardRepository.existsById(boardId)){
            boardRepository.deleteById(boardId);
            return true;
        } else return false;
    }
}
