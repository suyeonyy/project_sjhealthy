package com.example.sjhealthy.controller;

import com.example.sjhealthy.component.RecommendMapper;
import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.LikeRequest;
import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.dto.ResponseMessage;
import com.example.sjhealthy.entity.RecommendEntity;
import com.example.sjhealthy.service.RecommendService;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sjhealthy/")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @GetMapping({"/recommend", "/recommend/"})
    public String getRecommendList(@SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

        List<RecommendDTO> list = recommendService.getList();
        model.addAttribute("list", list);


        return "recommend/recList";
    }
    

    @GetMapping("/recommend/write")
    public String getRecommendForm(@SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

//        // 회원 전용 - 뷰에서 함.
//        if (loginId == null){
//            return "redirect:/sjhealthy/recommend";
//        }
        return "recommend/writeRec";
    }
/* 사용안함
    @PostMapping("/recommend/write")
    public String writeNewRecommendPost(@ModelAttribute RecommendDTO recommendDTO, RedirectAttributes ra,
                                        @SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

        try {
            RecommendDTO result = recommendService.addRecommendation(recommendDTO);

            if (result == null){
                System.out.println("추천글 등록에 실패하였습니다.");
            } else {
                System.out.println("추천글을 등록하였습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("시스템 오류");
        }
        return "redirect:/sjhealthy/recommend";
    }
*/
    @GetMapping("/recommend/read")
    public RecommendEntity readRecommendation(@RequestParam(name = "recId")Long recId,
                                              @SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

        RecommendEntity result = recommendService.readRecommendationById(recId);

        if (result == null){
            System.out.println("정보를 읽어오지 못했습니다.");
            return null;
        } else return result;
    }

    // 좋아요
    @ResponseBody
    @PostMapping("/recommend/like")
    public ResponseEntity<?> addLike(@RequestBody LikeRequest likeRequest,
                                   @SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

        if (likeRequest == null || likeRequest.getRecId() == null || likeRequest.getAction() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(
                "필드가 일치하지 않습니다.", null, null));
        }
        Long recId = likeRequest.getRecId();
        System.out.println("likeRequest = " + likeRequest);

//        좋아요 누르면 싫어요 눌렀던 거 사라짐(반대도 동일), ID당 1번 선택 가능
        boolean result = recommendService.handleLikeOrDislike(recId, loginId, "like");

        // 확인용
        System.out.println("recY/N = " + RecommendMapper.toRecommendDTO(recommendService.readRecommendationById(recId)));

        List<Tuple> list = recommendService.countLikeAndDislike(recId);
        Tuple tuple = list.get(0);
        Integer like = tuple.get("likeCount", Integer.class);
        Long likeCount = like.longValue();
        Integer dislike = tuple.get("dislikeCount", Integer.class);
        Long dislikeCount = dislike.longValue();


        if (result){
            return ResponseEntity.ok(new ResponseMessage("좋아요가 반영되었습니다.", likeCount, dislikeCount));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(
                "이미 좋아요를 누르셨습니다.", likeCount, dislikeCount));
        }
    }

    // 싫어요
    @ResponseBody
    @PostMapping("/recommend/dislike")
    public ResponseEntity<?> addDislike(@RequestBody LikeRequest likeRequest,
                                        @SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

        Long recId = likeRequest.getRecId();

        boolean result = recommendService.handleLikeOrDislike(recId, loginId, "dislike");

        //확인용
        System.out.println("recY/N = " + RecommendMapper.toRecommendDTO(recommendService.readRecommendationById(recId)));

        List<Tuple> list = recommendService.countLikeAndDislike(recId);
        Tuple tuple = list.get(0);
        Integer like = tuple.get("likeCount", Integer.class);
        Long likeCount = like.longValue();
        Integer dislike = tuple.get("dislikeCount", Integer.class);
        Long dislikeCount = dislike.longValue();


        if (result){
            return ResponseEntity.ok(new ResponseMessage("싫어요가 반영되었습니다.", likeCount, dislikeCount));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(
                "이미 싫어요를 누르셨습니다.", likeCount, dislikeCount));
        }
    }




    /*sy 작업*/
    @PostMapping("/recommend/write")
    public String writeNewRecommendPost(@ModelAttribute RecommendDTO recommendDTO, RedirectAttributes ra,
                                        @SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        try {
            RecommendDTO result = recommendService.addRecommendation(recommendDTO);

            if (result == null){
                System.out.println("추천글 등록에 실패하였습니다.");
            } else {
                System.out.println("추천글을 등록하였습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("시스템 오류");
        }
        return "redirect:/sjhealthy/recommend";
    }
}
