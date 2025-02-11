package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.GoogleProfile;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.dto.OAuthToken;
import com.example.sjhealthy.dto.Response;
import com.example.sjhealthy.entity.MemberEntity;
import com.example.sjhealthy.service.MailServiceImpl;
import com.example.sjhealthy.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RequestMapping("/sjhealthy/")
@Controller
public class LoginController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MailServiceImpl mailService;

    @Value("${CLIENT_ID}") // 이렇게 환경변수로 선언한 값을 불러와 사용
    private String client_id;

    @Value("${CLIENT_SECRET}")
    private String client_secret;

    @Value("${REDIRECT_URI}")
    private String redirect_uri;

    /*카카오 로그인*/
    @Value("${REST_API_KEY}")
    private String kakaoApiKey;

    @Value("${REDIRECT_KAKAO_URI}")
    private String kakaoRedirectUri;

    @Autowired
    private PasswordEncoder pwEncoder;

    @GetMapping("/member/login")
    public String showLoginPage(Model model, @RequestParam(name="message", required = false)String message){
        model.addAttribute("client_id", client_id);
        model.addAttribute("redirect_uri", redirect_uri);
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);
        return "login";
    }

    @ResponseBody
    @PostMapping("/member/login")
    public ResponseEntity<Response<Object>> loginFunction(@RequestBody Map<String, String> data, HttpSession session){
        String memberId = data.get("memberId");
        String memberPassword = data.get("memberPassword");

        // TODO: write나 다른 위치에서 온 경우엔 다시 그 위치로 보내는 로직
        try {
            MemberDTO loginResult = memberService.login(new MemberDTO(memberId, memberPassword));

            if (loginResult != null){
    //                로그인 성공
                session.setAttribute("loginId", loginResult.getMemberId());
                return ResponseEntity.ok(new Response<>(null, "로그인에 성공했습니다."));
            } else {
    //                로그인 실패
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "로그인에 실패하였습니다."));
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "시스템 오류로 로그인에 실패하였습니다."));
        }
    }

    /* 카카오 로그인 */
    @GetMapping("/member/login/oauth/kakao")
    public String signin(@RequestParam("code") String code, HttpServletRequest request, RedirectAttributes ra, Model model) throws JsonProcessingException {
        // 1. 카카오 토큰 API에 인증 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);

        //회원 탈퇴 시, 카카오의 “연동 해제” API를 호출하기 위해 로그인 시 받아온 액세스 토큰을 세션에 보관
        HttpSession session = request.getSession();
        session.setAttribute("accessToken_kakao", accessToken);

        // 2. 액세스 토큰을 사용하여 사용자 정보 요청
        String userInfo = getKakaoUserInfo(accessToken);

        // ObjectMapper 인스턴스를 생성하여 JSON을 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(userInfo);  // JSON 문자열을 JsonNode로 파싱

        // kakao_account에서 이메일을 추출
        JsonNode kakaoAccountNode = rootNode.path("kakao_account");  // "kakao_account" 필드 추출
        String email = kakaoAccountNode.path("email").asText();  // 이메일 추출

        MemberDTO isExist = memberService.findMemberEmail(email);
        if (isExist != null){
            // 기존 회원이라면 로그인 처리 후 메인으로 넘김
            //HttpSession session = request.getSession();
            session.setAttribute("loginId", isExist.getMemberId());
            return "redirect:/sjhealthy";
        } else {
            // 비회원은 가입창으로 연결
            // 회원정보를 가져와 회원가입 뷰로 보냄
            MemberDTO kakaoAccount = new MemberDTO();
            kakaoAccount.setMemberId(email.split("@")[0]); // 이메일의 @ 앞부분을 가져와 memberId로 설정
            kakaoAccount.setMemberEmail(email);

            String emailFirst = email.split("@")[0]; // 이메일의 @ 앞부분
            String emailLast = email.split("@")[1]; // 이메일의 @ 뒷부분

            kakaoAccount.setMemberEmailFirst(emailFirst);
            kakaoAccount.setMemberEmailLast(emailLast);

            model.addAttribute("memberDTO", kakaoAccount); // 이 3가지 정보를 회원가입 창에 채워서 뷰 출력
            return "join";
        }
    }

    private String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String clientId = kakaoApiKey;  // 카카오 REST API 키
        String redirectUri = kakaoRedirectUri;  // 카카오 Redirect URI

        // 요청 파라미터 설정
        String url = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .toUriString();

        // REST API 요청을 위한 RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 카카오 API에 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);

        // 응답에서 액세스 토큰 추출
        String accessToken = extractAccessToken(response.getBody());
        return accessToken;
    }

    private String getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";  // 카카오 사용자 정보 API URL

        // 액세스 토큰을 Authorization 헤더에 넣어 요청
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);  // "Bearer <access_token>"
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate을 이용해 카카오 사용자 정보 API에 GET 요청
        RestTemplate restTemplate = new RestTemplate();

        try {
            // 카카오 사용자 정보 요청
            ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
            // 응답 결과 반환
            return response.getBody();  // 응답 본문을 반환

        } catch (HttpClientErrorException e) {
            // HTTP 에러 발생 시 에러 메시지 출력
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // 기타 예외 처리
            e.printStackTrace();
            return null;
        }
    }

    private String extractAccessToken(String jsonResponse) {
        try {
            // JSON 응답을 파싱하여 access_token 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            return jsonNode.get("access_token").asText();  // access_token 값을 반환
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @GetMapping("/member/delete/kakao/{memberId}")
    public ResponseEntity<Response<Object>> deleteKakaoMember(
            @PathVariable("memberId") String memberId, //요청 URL의 일부로 포함된 값을 메서드 파라미터에 바인딩
            HttpServletRequest request, //클라이언트의 HTTP 요청에 관한 모든 정보를 담고 있는 객체
            @SessionAttribute(name = "accessToken_kakao", required = false) String accessToken){

        //1. 회원 아이디 및 액세스 토큰 유효성 확인
        if(memberId == null || memberId.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "아이디가 존재하지 않습니다."));
        }
        if(accessToken == null || accessToken.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response<>(null, "카카오 액세스 토큰이 존재하지 않습니다."));
        }

        //2. 카카오 연동 해제 API 호출
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink"; //카카오 계정과 애플리케이션 간의 연결을 해제하는 요청을 보내기 위한 주소
        HttpHeaders headers = new HttpHeaders(); //인증을 위한 헤더를 설정하여 카카오 서버가 요청을 승인할 수 있도록
        headers.set("Authorization", "Bearer " + accessToken); //"Authorization" 헤더:인증 정보를 전달하는 표준 방법, "Bearer " 접두사와 함께 액세스 토큰을 보냄
        HttpEntity<String> entity = new HttpEntity<>(headers); //설정된 HTTP 헤더를 포함한 요청 본문(여기서는 본문이 필요 없으므로 헤더만 포함)을 하나의 엔티티로 래핑

        try{
            RestTemplate restTemplate = new RestTemplate(); //RestTemplate : 외부 HTTP 요청을 보내고 응답을 받을 때 사용하는 클라이언트
            ResponseEntity<String> response = restTemplate.exchange(
                    unlinkUrl, HttpMethod.POST, entity, String.class);
                    //카카오 연동 해제 API의 엔드포인트 URL
                    //카카오의 연동 해제 API에 POST 요청
                    //HTTP 요청에 필요한 헤더(예: "Authorization" 헤더)가 포함
                    //API 응답으로 받을 데이터의 타입을 지정, 응답 본문을 문자열(String)로 받겠다는 의미
                    //exchange메서드 : HTTP POST 요청을 카카오 API에 보내고, 호출 결과는 ResponseEntity<String> 타입으로 반환
            System.out.println("카카오 연동 해제 응답: " + response.getBody());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(null, "카카오 연동 해제에 실패하였습니다."));
        }

        //3. 3. DB에서 회원 정보 삭제 처리
        memberService.delete(memberId);

        // 4. 세션 무효화 (로그아웃)
        HttpSession session = request.getSession();
        session.invalidate();

        return ResponseEntity.ok(new Response<>(null, "카카오 연동 회원 탈퇴가 완료되었습니다."));
    }

    // 구글 로그인 연동
    @GetMapping("/member/login/oauth/google")
    public String OAuthGoogle(@RequestParam("code") String code, HttpServletRequest request, RedirectAttributes ra, Model model) throws JsonProcessingException {
        OAuthToken token = getAccessTokenWithGoogle(code, request);
        GoogleProfile profile = requestGoogleAccountProfile(token);

        try {
            MemberDTO isExist = memberService.findMemberEmail(profile.getEmail());
            if (isExist != null){
                // 기존 회원이라면 로그인 처리 후 메인으로 넘김
                HttpSession session = request.getSession();
                session.setAttribute("loginId", isExist.getMemberId());
                return "redirect:/sjhealthy";
            } else {
                // 비회원은 가입창으로 연결
                // 회원정보를 가져와 회원가입 뷰로 보냄
                MemberDTO googleAccount = new MemberDTO();
                googleAccount.setMemberId(profile.getEmail().split("@")[0]); // 이메일의 @ 앞부분을 가져와 memberId로 설정
                googleAccount.setMemberEmail(profile.getEmail());
                googleAccount.setMemberName(profile.getName());

                String email = profile.getEmail();
                String emailFirst = email.split("@")[0]; // 이메일의 @ 앞부분
                String emailLast = email.split("@")[1]; // 이메일의 @ 뒷부분

                googleAccount.setMemberEmailFirst(emailFirst);
                googleAccount.setMemberEmailLast(emailLast);

                model.addAttribute("memberDTO", googleAccount); // 이 3가지 정보를 회원가입 창에 채워서 뷰 출력
                return "join";
            }

        } catch (Exception e){
            e.printStackTrace();
            //redirect 혈식으로 return 시, 이전에 설정된 model값은 사라짐
            return "redirect:/sjhealthy/member/login";
        }
    }

    public OAuthToken getAccessTokenWithGoogle(String code, HttpServletRequest request){
        // 받아온 인가 코드로 액세스 토큰 요청
        // POST 방식으로 key-value 데이터를 요청하고 액세스 토큰 받아옴
        RestTemplate rt = new RestTemplate();

        // Headers
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Body
        String grant_type = "authorization_code";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", client_id);
        params.add("client_secret", client_secret);
        params.add("redirect_uri", redirect_uri);
        params.add("grant_type", grant_type);

        // 한 곳에 담는다
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
            "https://oauth2.googleapis.com/token",
            HttpMethod.POST,
            googleTokenRequest,
            String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        OAuthToken token = new OAuthToken();

        try {
            token = mapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 토큰을 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute("accessToken_google", token.getAccess_token());
        return token;
    }

    public GoogleProfile requestGoogleAccountProfile(OAuthToken token) throws JsonProcessingException {
        // 토큰을 담아 정보 요청
        RestTemplate rt2 = new RestTemplate();

        HttpHeaders headers1 = new HttpHeaders();
        // 토큰은 헤더에 숨겨 보낸다
        headers1.add(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccess_token());

        HttpEntity<MultiValueMap<String,String>> googleAccountRequest = new HttpEntity<>(headers1);
        ResponseEntity<String> response = rt2.exchange(
            "https://www.googleapis.com/userinfo/v2/me",
            HttpMethod.GET,
            googleAccountRequest,
            String.class
        );

        ObjectMapper mapper = new ObjectMapper();

        GoogleProfile profile = mapper.readValue(response.getBody(), GoogleProfile.class);
        return profile;
    }

    @GetMapping("/member/find-id")
    public String findIdForm(){
        return "findId";
    }

    @PostMapping("/member/find-id")
    public String findIdAfterPost(@ModelAttribute MemberDTO memberDTO, Model model){ // 이름, 생년월일로 찾기
        try {
            MemberDTO findIdResult = memberService.findMemberId(memberDTO);

            if (findIdResult != null){
                model.addAttribute("memberDTO", findIdResult);
            } else {
                model.addAttribute("dto", 1);
                model.addAttribute("memberDTO", findIdResult);
            }
            return "findId"; // 양식만 바꿔 같은 뷰 사용

        } catch (Exception e){
            return "redirect:/sjhealthy/member/login";
        }
    }

    @GetMapping("/member/find-password")
    public String findPasswordForm(){
        return "findPassword";
    }

    @ResponseBody
    @PostMapping("/member/find-password/check")
    public ResponseEntity<Response<Object>> checkCode(@RequestBody Map<String, String> data,
                                                      @SessionAttribute(name = "mailCode", required = false) String mailCode) {
        try {
            String code = data.get("code");
            String memberId = data.get("memberId");
            String memberEmail = data.get("memberEmail");

            MemberDTO dto = memberService.findMemberIdAtPassFind(memberId);
            if (dto == null) {
                // 존재하지 않는 아이디
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "아이디가 존재하지 않습니다."));
            } else if (!dto.getMemberEmail().equals(memberEmail)) {
                // 아이디는 존재하나 입력한 이메일과 다를 때
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "등록한 이메일과 일치하지 않습니다."));
            } else {
                if (code.equals(mailCode)) {
                    return ResponseEntity.ok(new Response<>(null, "인증번호가 일치합니다."));
                } else
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "인증번호가 일치하지 않습니다."));
            }
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "시스템 오류로 실패하였습니다."));
        }
    }

    @GetMapping("/member/set-new-password")
    public String openSetNewPasswordForm(){
        return "setNewPassword";
    }
    @PostMapping("/member/set-new-password")
    public String setNewPassword(@RequestParam("password")String password, RedirectAttributes ra,
//                                 @RequestParam("passwordCheck")String passwordCheck,
                                 @SessionAttribute(name = "loginId", required = false) String memberId){
        try {
            MemberDTO beforeUpdate = memberService.findMemberIdAtPassFind(memberId);
            if (!pwEncoder.matches(password, beforeUpdate.getMemberPassword())){
                beforeUpdate.setMemberPassword(password);
                memberService.changePassword(beforeUpdate); // 바꾼 비밀번호로 정보 수정(JPA 에선 save로 등록과 수정을 한다)
                ra.addFlashAttribute("changePassMessage", "비밀번호를 변경하였습니다.");
                return "redirect:/sjhealthy/member/login";
            } else {
                // 기존 비밀번호와 동일하면 돌려보냄
                ra.addFlashAttribute("changePassMessage", "기존 비밀번호와 동일한 비밀번호입니다.");
                return "redirect:/sjhealthy/member/login";
            }
        } catch (Exception e){
            ra.addFlashAttribute("changePassMessage", "시스템 오류로 실패하였습니다.");
            e.printStackTrace();
            return "redirect:/sjhealthy/member/login";
        }
    }

    @RequestMapping("/member/logout")
    public String logout(@SessionAttribute(name = "loginId", required = false) String loginId, Model model,
                         HttpSession session){
        model.addAttribute("loginId", loginId);

        if (loginId != null){
            session.invalidate(); // 세션 무효화
        }
        return "redirect:/sjhealthy/member/login";
    }

    @GetMapping("/member/delete")
    public String getDeleteForm(@SessionAttribute(name = "loginId", required = false) String loginId, Model model){
        model.addAttribute("loginId", loginId);

        return "deleteMember";
    }

    @GetMapping("/member/delete/{memberId}")
    public String deleteMember(@PathVariable String memberId, RedirectAttributes ra){
        try {
            if (memberId != null && memberService.findMemberEmail(memberId) != null){
                memberService.delete(memberId);
                ra.addAttribute("message", "탈퇴 완료되었습니다.");
                return "redirect:/sjhealthy";
            }
            ra.addAttribute("message", "탈퇴하지 못했습니다.");
            return "redirect:/sjhealthy";
        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/sjhealthy";
        }
    }

    // 구글 연동 회원 탈퇴
    @ResponseBody
    @GetMapping("/member/delete/google/{memberId}")
    public ResponseEntity<?> deleteGoogleMember(@PathVariable ("memberId") String memberId, HttpServletRequest request,
                                                @SessionAttribute(name = "accessToken_google", required = false)String accessToken){
        if (memberId == null || memberId.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(null, "아이디가 존재하지 않습니다."));
        }
        // 연동 해제를 위한 액세스 토큰 보냄
        // 회원 정보로 조회해서 확인하고 그런 과정 있으면 좋은데 나중에 해야지

        // 제대로 받았으면 삭제 처리
        memberService.delete(memberId);
        // 탈퇴하면 deleted 를 Y로 바꿔서 재가입 막든지 그런 세부사항은 의논
        System.out.println("탈퇴 완료");
        // 세션 무효화
        HttpSession session = request.getSession();
        session.invalidate();


        return ResponseEntity.ok(accessToken);
    }


/*
    @GetMapping("../login/isLogged")
    public String homePage(Model model, HttpSession session) {
        boolean isLoggedIn = session.getAttribute("user") != null; // 예시로 세션에 'user' 객체가 있으면 로그인된 상태로 간주
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "header"; // Thymeleaf 템플릿 이름
    }
 */
}

