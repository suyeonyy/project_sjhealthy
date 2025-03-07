package com.example.sjhealthy.controller;

import com.example.sjhealthy.component.RecommendMapper;
import com.example.sjhealthy.dto.*;
import com.example.sjhealthy.entity.RecommendEntity;
import com.example.sjhealthy.service.MemberService;
import com.example.sjhealthy.service.RecommendService;
import jakarta.persistence.Tuple;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.lang.Integer.parseInt;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/sjhealthy/")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @Autowired
    private MemberService memberService;

    @Value("${JS_APPKEY}")
    private String js_appKey;

    @GetMapping({"/recommend", "/recommend/"})
    public String openRecBoardForm(@SessionAttribute(name = "loginId", required = false)String loginId, Model model,
                                   RedirectAttributes ra){
        model.addAttribute("loginId", loginId);

        List<RecommendDTO> dto = recommendService.getList();
        model.addAttribute("list", dto);

        if (loginId != null){
            MemberDTO member = memberService.findMemberIdAtPassFind(loginId);

            if (member.getMemberAuth().equals("A")){
                model.addAttribute("administrator", true);
            }
        }

        return "recommend/recList";
    }

    @GetMapping("/recommend/list")
    public ResponseEntity<PagedModel<EntityModel<RecommendDTO>>> getRecommendList(
            @SessionAttribute(name = "loginId", required = false)String loginId, Model model,
            @RequestParam(name="page",defaultValue = "1") int page, PagedResourcesAssembler<RecommendDTO> assembler){
        model.addAttribute("loginId", loginId);

        int size = 10;

        try {
            Page<RecommendDTO> list = recommendService.getListWithPage(page, size);

            if (!list.isEmpty()){
                // 추천글이 있을 때
                PagedModel<EntityModel<RecommendDTO>> recList = assembler.toModel(list);
                return ResponseEntity.ok(recList);
            } else {
                // 추천글이 없을 때
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/recommend/best")
    public ResponseEntity<Response<Object>> getRecommendListTop5LikeCount(){
        try {
            List<RecommendDTO> dtoList = recommendService.getListTop4();
//            if (dtoList != null){ 이걸로 검색하면 List는 걸러지지 않는다
            if (!dtoList.isEmpty()){
                return ResponseEntity.ok(new Response<>(dtoList, "추천글을 불러왔습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null, "추천글이 존재하지 않습니다."));
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "시스템 오류로 실패하였습니다."));
        }
    }
    @GetMapping("/recommend/write")
    public String getRecommendForm(@SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);
        model.addAttribute("js_appKey", js_appKey);

        return "recommend/writeRec";
    }

    @ResponseBody
    @GetMapping("/recommend/read")
    public ResponseEntity<Response<Object>> readRecommendation(@RequestParam("recId") Long recId,
                                              @SessionAttribute(name = "loginId", required = false)String loginId, Model model,
                                              HttpServletRequest request, HttpServletResponse response){
        model.addAttribute("loginId", loginId);

        if (loginId == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        RecommendEntity result = recommendService.readRecommendationById(recId);
        System.out.println(result);

        if (result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(null, "정보를 읽어오지 못했습니다."));
        } else {
            // 조회수, 브라우저 종료시 다시 집계됨
            Cookie viewCount = addViews(request, recId);
            if (viewCount != null){
                response.addCookie(viewCount);
            }
            RecommendDTO dto = RecommendMapper.toRecommendDTO(result, loginId);
            return ResponseEntity.ok().body(new Response<>(dto, "정보를 읽어왔습니다."));
        }
    }

    // 상세페이지 조회수
    public Cookie addViews(HttpServletRequest request, Long recId){
        Cookie[] cookies = request.getCookies();

        if (cookies == null){
            recommendService.countRecViews(recId);
            // 쿠키가 없다면 생성
            Cookie newCookie = new Cookie("view_count", recId.toString());
            newCookie.setPath("/");
            return newCookie;
        } else {
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("view_count")){
                    if (!cookie.getValue().contains(recId.toString())){
                        // 조회 기록 없을시 조회수 1 증가
                        recommendService.countRecViews(recId);
                        cookie.setValue(cookie.getValue() + "_" + recId);
                        return cookie;
                    } else return null;
                }
            }
        }
        // 쿠키는 있는데 view_count가 없을 때
        recommendService.countRecViews(recId);
        Cookie newCookie = new Cookie("view_count", recId.toString());
        newCookie.setPath("/");
        return newCookie;
    }

    // 좋아요
    @ResponseBody
    @PostMapping("/recommend/like")
    public ResponseEntity<?> addLike(@RequestBody LikeRequest likeRequest,
                                   @SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

        if (likeRequest == null || likeRequest.getRecId() == null || likeRequest.getAction() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("필드가 일치하지 않습니다."));
        }
        Long recId = likeRequest.getRecId();

//        좋아요 누르면 싫어요 눌렀던 거 사라짐(반대도 동일), ID당 1번 선택 가능
        boolean result = recommendService.handleLikeOrDislike(recId, loginId, "like");

        // 확인용
        System.out.println("recY/N = " + RecommendMapper.toRecommendDTO(recommendService.readRecommendationById(recId), loginId));

        // 집계
        List<Tuple> list = recommendService.countLikeAndDislike(recId);
        Tuple tuple = list.get(0);
        Integer like = tuple.get("likeCount", Integer.class);
        Long likeCount = like.longValue();
        Integer dislike = tuple.get("dislikeCount", Integer.class);
        Long dislikeCount = dislike.longValue();

        if (result){
            return ResponseEntity.ok(new ResponseMessage("좋아요가 반영되었습니다.", likeCount, dislikeCount));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("이미 좋아요를 누르셨습니다."));
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
        System.out.println("recY/N = " + RecommendMapper.toRecommendDTO(recommendService.readRecommendationById(recId), loginId));

        // 집계
        List<Tuple> list = recommendService.countLikeAndDislike(recId);
        Tuple tuple = list.get(0);
        Integer like = tuple.get("likeCount", Integer.class);
        Long likeCount = like.longValue();
        Integer dislike = tuple.get("dislikeCount", Integer.class);
        Long dislikeCount = dislike.longValue();

        if (result){
            return ResponseEntity.ok(new ResponseMessage("싫어요가 반영되었습니다.", likeCount, dislikeCount));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("이미 싫어요를 누르셨습니다."));
        }
    }

    @ResponseBody
    @GetMapping("/recommend/detail/{recId}")
    public ResponseEntity<Response<Object>> getLikeDislikeCount(@PathVariable("recId") Long recId, HttpServletRequest request,
                                                                @SessionAttribute(name = "loginId", required = false)String loginId,
                                                                HttpServletResponse response, Model model){
        model.addAttribute("loginId", loginId);

        if (loginId == null) {
            // 회원 아니면 상세보기 불가능
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response<>(null, "회원 전용 기능입니다."));
        }

        // 상세페이지에서 좋아요 싫어요 개수만 가져오는 용도 + 상세페이지 내용도 포함함 ( 그냥 이걸로 다 하자)
        List<Tuple> list = recommendService.countLikeAndDislike(recId);
        Tuple tuple = list.get(0);
        Integer like = tuple.get("likeCount", Integer.class);
        Long likeCount = like.longValue();
        Integer dislike = tuple.get("dislikeCount", Integer.class);
        Long dislikeCount = dislike.longValue();

        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(
                null, "집계 오류", likeCount, dislikeCount));
        } else {
            RecommendDTO dto = RecommendMapper.toRecommendDTO(recommendService.readRecommendationById(recId), loginId);
            MemberDTO member = memberService.findMemberIdAtPassFind(loginId);

            Cookie viewCount = addViews(request, recId);
            if (viewCount != null){
                response.addCookie(viewCount);
            }

            if (member.getMemberAuth().equals("A")){ // 관리자
                return ResponseEntity.ok(new Response<>(dto, "집계 완료", likeCount, dislikeCount, member));
            } else { // 일반 회원
                // 조회수, 브라우저 종료시 다시 집계됨

                return ResponseEntity.ok(new Response<>(dto, "집계 완료", likeCount, dislikeCount));
            }
        }

    }

    //@ResponseBody 이거 붙이면 redirect 작동안함.@ResponseBody 활성화되면 반환된 값은 리다이렉트와 같은 뷰 이름이 아닌 직접 HTTP 본문으로 처리되기 때문.
    @PostMapping("/recommend/write")
    public ResponseEntity<String> writeNewRecommendPost(@ModelAttribute RecommendDTO recommendDTO, RedirectAttributes ra,
                                        @SessionAttribute(name = "loginId", required = false)String loginId, Model model){
        model.addAttribute("loginId", loginId);

        try {
            RecommendDTO result = recommendService.addRecommendation(recommendDTO);

            if (result == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("/sjhealthy/recommend");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("/sjhealthy/recommend");
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("/sjhealthy/recommend");
        }

        //ra.addAttribute("loginId", loginId); //addAttribute로 보내면 리다이렉트 값 유지못함
//        ra.addFlashAttribute("loginId", loginId);

        // 리다이렉트 URL을 JSON 형태로 응답
//        return ResponseEntity.ok("/sjhealthy/recommend");

        /*참고: AJAX를 사용한 POST 요청 후 리다이렉트가 작동하지 않는 문제는 AJAX 요청 자체가 페이지 리다이렉트를 처리하지 않기 때문*/
        //return "redirect:/sjhealthy/recommend";
    }

    // 추천글 작성시 가게-메뉴 중복 확인
    @ResponseBody
    @PostMapping("/recommend/write/check")
    public ResponseEntity<Response<Object>> checkMenu(@RequestBody Map<String, String> data){
        String rec = data.get("recStore");
        String recMenu = data.get("recMenu");

        RecommendEntity result;
        try {
            if (rec.contains(" ")){ // 지점이 여러 개인 프랜차이즈일 때
                String recStore = rec.split(" ")[0]; // 지점 뺀 앞 부분만
                result = recommendService.checkByRecStoreAndRecMenu(recStore, recMenu);
            } else {
                result = recommendService.checkByRecStoreAndRecMenu(rec, recMenu);
            }

            if (result == null){ // 중복이 아니면 204로 리턴
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(recMenu, "등록 가능합니다."));
            } else {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new Response<>(null, "중복되는 데이터가 존재합니다.\n추천을 원하시면 해당 추천글에 좋아요를 눌러주세요."));
            }

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "시스템 오류"));
        }
    }

//    @ResponseBody
//    @GetMapping("/recommend/sort/{storeName}") // url 경로에서 storeName 추출 => @PathVariable 사용
//    public ResponseEntity<List<RecommendDTO>> SearchByStoreName(@PathVariable String storeName, @SessionAttribute(name = "loginId", required = false)String loginId){
//
//        if (storeName == null || storeName.isEmpty()){
//            return ResponseEntity.badRequest().build();
//            // build() : ResponseEntity 객체를 생성할 때 body는 빈 상태로 HTTP 상태 코드만 설정 가능
//        }
//        List<RecommendEntity> bySearch = recommendService.getListByPlaceName(storeName);
//        List<RecommendDTO> dto = new ArrayList<>();
//        for (RecommendEntity s : bySearch){
//            dto.add(RecommendMapper.toRecommendDTO(s, loginId));
//        }
//        return ResponseEntity.ok(dto);
//    }

    @ResponseBody // 페이지네이션 추가
    @PostMapping("/recommend/sort") // url 경로에서 storeName 추출 => @PathVariable 사용
    public ResponseEntity<PagedModel<EntityModel<RecommendDTO>>> SearchByStoreName(@RequestBody Map<String, String> data,
                                                                @SessionAttribute(name = "loginId", required = false)String loginId,
                                                                PagedResourcesAssembler<RecommendDTO> assembler){
        int size = 10 ;
        String storeName = data.get("storeName");
        int page = parseInt(data.get("page"));
        System.out.println("page=" + page);
        System.out.println("storeName=" + storeName);

        if (storeName == null || storeName.isEmpty()){
            return ResponseEntity.badRequest().build();
            // build() : ResponseEntity 객체를 생성할 때 body는 빈 상태로 HTTP 상태 코드만 설정 가능
        }
        try {
            Page<RecommendDTO> list = recommendService.getListByPlaceNameWithPage(storeName, page, size);
            System.out.println("검색 리스트 = " + list.getContent());
            if (!list.isEmpty()){
                // 추천글이 있을 때
                PagedModel<EntityModel<RecommendDTO>> recList = assembler.toModel(list);
                return ResponseEntity.ok(recList);
            } else {
                // 추천글이 없을 때
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping("/recommend/delete/{recId}")
    public String deleteRecommend(@PathVariable("recId") Long recId, RedirectAttributes ra){
        try {
            boolean result = recommendService.delete(recId);

            if (result){
                ra.addAttribute("message", "해당 게시물이 삭제되었습니다.");
                return "redirect:/sjhealthy/recommend";
//                return ResponseEntity.ok().body(new Response<>(null, "해당 게시물이 삭제되었습니다."));
            } else {
                ra.addAttribute("message", "존재하지 않는 게시물입니다.");
                return "redirect:/sjhealthy/recommend";
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response<>(null, "존재하지 않는 게시물입니다."));
            }
        } catch (Exception e){
            ra.addAttribute("message", "시스템 오류로 실패하였습니다.");
            return "redirect:/sjhealthy/recommend";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "시스템 오류로 실패하였습니다."));
        }
    }
}
