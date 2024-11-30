package com.example.sjhealthy.controller;

import com.example.sjhealthy.component.RecommendMapper;
import com.example.sjhealthy.dto.BoardDTO;
import com.example.sjhealthy.dto.RecommendDTO;
import com.example.sjhealthy.entity.RecommendEntity;
import com.example.sjhealthy.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/recommend/read")
    public RecommendEntity readRecommendation(@RequestParam(name = "recId")Long recId){
        RecommendEntity result = recommendService.readRecommendationById(recId);

        if (result == null){
            System.out.println("정보를 읽어오지 못했습니다.");
            return null;
        } else return result;
    }

    @ResponseBody
    @GetMapping("/recommend/like")
    public RecommendEntity addLike(@RequestBody Long recY, @RequestBody Long recId){
        RecommendDTO dto = RecommendMapper.toRecommendDTO(recommendService.readRecommendationById(recId));
//        좋아요 누르면 싫어요 눌렀던 거 사라짐(반대도 동일), ID당 1번 선택 가능
        // 아직 못 만듦. 내일 다시
        dto.setRecY(recY);

        RecommendEntity entity = RecommendMapper.toRecommendEntity(recommendService.addRecommendation(dto));
        return entity;
    }
}
