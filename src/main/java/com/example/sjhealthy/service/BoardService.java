package com.example.sjhealthy.service;

import com.example.sjhealthy.component.BoardMapper;
import com.example.sjhealthy.component.MemberMapper;
import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.entity.BoardEntity;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.repository.BoardRepository;
import com.example.sjhealthy.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    private final MemberRepository memberRepository;

    public List<BoardDTO> getList(){
        // board_id 기준으로 내림차순 정렬
//        Sort sort = Sort.by(Sort.Order.desc("boardId"));
//        List<BoardEntity> boardList = boardRepository.findAll();
        List<BoardEntity> boardList = boardRepository.findAllByOrderByBoardIdDesc();

        List<BoardDTO> list = new ArrayList<>();
        for (BoardEntity post : boardList){
            String memberId = post.getMember() != null ? post.getMember().getMemberId() : null;
            list.add(BoardMapper.toBoardDTO(post, memberId));
        }
        return list;
    }
    public Page<BoardDTO> getListWithPage(int page, int size){ // 페이징
        Pageable pageable = PageRequest.of(page-1, size); // page는 1부터 시작하니까 -1 해서 가져옴

        Page<BoardEntity> entity = boardRepository.findAllByOrderByBoardIdDesc(pageable);
        return BoardMapper.convertToBoardDTOPage(entity);

    }
    public BoardDTO write(BoardDTO boardDTO){ // update도 이걸로 사용
        Optional<MemberEntity> entity = memberRepository.findByMemberId(boardDTO.getMemberId());
        if (entity.isPresent()){
            MemberEntity memberEntity = entity.get();
            BoardEntity postEntity = BoardMapper.toBoardEntity(boardDTO, memberEntity);
            // 저장
            BoardEntity savedEntity = boardRepository.save(postEntity);
            // 다시 dto로 변환해 반환
            return BoardMapper.toBoardDTO(savedEntity, savedEntity.getMember().getMemberId());
        } else return null;
    }

    public BoardDTO read(Long boardId){
        Optional<BoardEntity> entity = boardRepository.findById(boardId);

        if (entity.isPresent()){
            BoardEntity readEntity = entity.get();
            return BoardMapper.toBoardDTO(readEntity, readEntity.getMember().getMemberId());
        } else return null;
    }

    public boolean delete(Long boardId){
        if (boardRepository.existsById(boardId)){
            boardRepository.deleteById(boardId);
            return true;
        } else return false;
    }

    public void countBoardView(BoardDTO boardDTO){
        System.out.println("증가 전 " + boardDTO.getBoardViews());
        boardDTO.setBoardViews(boardDTO.getBoardViews()+1);
        System.out.println("증가 후 " + boardDTO.getBoardViews());

        Optional<MemberEntity> entity = memberRepository.findByMemberId(boardDTO.getMemberId());
        if (entity.isPresent()){
            MemberEntity memberEntity = entity.get();
            BoardEntity postEntity = BoardMapper.toBoardEntity(boardDTO, memberEntity);
            boardRepository.save(postEntity);
        }
    }
}
