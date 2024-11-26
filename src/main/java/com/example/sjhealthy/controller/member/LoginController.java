package com.example.sjhealthy.controller.member;

import com.example.sjhealthy.dto.GoogleProfile;
import com.example.sjhealthy.dto.MemberDTO;
import com.example.sjhealthy.dto.OAuthToken;
import com.example.sjhealthy.service.MailServiceImpl;
import com.example.sjhealthy.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/sjhealthy/")
@Controller
public class LoginController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MailServiceImpl mailService;

    @GetMapping("/member/login")
    public String showLoginPage(){
        System.out.println("loginForm");
        return "login";
    }

    @PostMapping("/member/login")
    public String loginFunction(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        System.out.println("login");

        // TODO: write나 다른 위치에서 온 경우엔 다시 그 위치로 보내는 로직
        try {
            MemberDTO loginResult = memberService.login(memberDTO);
            if (loginResult != null){
//                로그인 성공
                session.setAttribute("loginId", loginResult.getMemberId());
                System.out.println("로그인 성공");
                return "redirect:/sjhealthy";
            } else {
//                로그인 실패
                System.out.println("로그인 실패");
                return "redirect:/sjhealthy/member/login";
            }
        } catch (Exception e){
            System.out.println("시스템 오류");
            return "redirect:/sjhealthy";
        }
    }

    @GetMapping("/member/login/oauth/google")
    public String OAuthGoogle(String code, HttpServletRequest request, RedirectAttributes ra, Model model) throws JsonProcessingException {
        OAuthToken token = getAccessTokenWithGoogle(code, request);
        System.out.println("토큰 " + token);
        GoogleProfile profile = requestGoogleAccountProfile(token);
        System.out.println(profile);

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
            System.out.println("시스템 오류");
            e.printStackTrace();
            //redirect 혈식으로 return 시, 이전에 설정된 model값은 사라짐
            return "redirect:/sjhealthy/member/login";
        }
    }

    public OAuthToken getAccessTokenWithGoogle(String code, HttpServletRequest request){
        System.out.println(code);
        // 받아온 인가 코드로 액세스 토큰 요청
        // POST 방식으로 key-value 데이터를 요청하고 액세스 토큰 받아옴
        RestTemplate rt = new RestTemplate();

        // Headers
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // Body
        String client_id = "***REMOVED***";
        String client_secret = "***REMOVED***";
        String redirect_uri = "***REMOVED***";
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
        System.out.println("token: " + token);

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
        System.out.println(response);

        GoogleProfile profile = mapper.readValue(response.getBody(), GoogleProfile.class);
        return profile;
    }

    @GetMapping("/member/find-id")
    public String findIdForm(){
        System.out.println("find-id Form");

        return "findId";
    }

    @PostMapping("/member/find-id")
    public String findIdAfterPost(@ModelAttribute MemberDTO memberDTO, Model model){ // 이름, 생년월일로 찾기
        try {
            MemberDTO findIdResult = memberService.findMemberId(memberDTO);

            if (findIdResult != null){
                System.out.println("아이디: " + memberDTO.getMemberId());
                model.addAttribute("memberDTO", findIdResult);
            }
            return "findId"; // 양식만 바꿔 같은 뷰 사용

        } catch (Exception e){
            System.out.println("시스템 오류");
            return "redirect:/sjhealthy/member/login";
        }
    }

    @GetMapping("/member/find-password")
    public String findPasswordForm(){
        System.out.println("find-password Form");

        return "findPassword";
    }

//    @GetMapping("/member/find-password")
//    인증메일을 입력한 아이디를 조회해서 해당 계정의 이메일인지 확인하는 과정 필요
    @PostMapping("/member/find-password")
    public String findPasswordAfterPost(@ModelAttribute MemberDTO memberDTO, Model model,
                                        @SessionAttribute(name = "mailCode", required = false) String mailCode,
                                        @SessionAttribute(name = "memberId", required = false) String memberId,
                                        @RequestParam(name = "verificationCode")String inputCode){
        // 컨트롤러에서 아이디 존재하는지 확인 후 메일 인증 진행
        // 메일로 인증메일 발송 (이 과정은 RestController와 js로)
        try {
            System.out.println(memberId + " " + mailCode);
            MemberDTO byMemberId = memberService.findMemberIdAtPassFind(memberId);
            System.out.println(byMemberId);

            if (byMemberId != null){
                if (mailCode.equals(inputCode)){
                    System.out.println("입력 코드: " + inputCode);
                    System.out.println("인증 코드: " + mailCode);
                    return "setNewPassword";
                } else {
                    System.out.println("코드가 일치하지 않습니다.");
                }
            } else {
                System.out.println("아이디가 존재하지 않습니다.");
            }
        } catch (Exception e){
            System.out.println("시스템 오류");
        }
        return "redirect:/sjhealthy/member/login";
    }


    @PostMapping("/member/set-new-password")
    public String setNewPassword(@RequestParam("password")String password, RedirectAttributes ra,
//                                 @RequestParam("passwordCheck")String passwordCheck,
                                 @SessionAttribute(name = "memberId", required = false) String memberId){
        System.out.println(password);

        try {
            MemberDTO beforeUpdate = memberService.findMemberIdAtPassFind(memberId);
            // 뷰에서 일치 검사
            if (!password.equals(beforeUpdate.getMemberPassword())){
                beforeUpdate.setMemberPassword(password);
                memberService.join(beforeUpdate); // 바꾼 비밀번호로 정보 수정(JPA 에선 save로 등록과 수정을 한다)
                ra.addAttribute("비밀번호를 변경하였습니다.");
                return "redirect:/sjhealthy/member/login";
            } else {
                // 기존 비밀번호와 동일하면 돌려보냄
                System.out.println("기존 비밀번호와 동일한 비밀번호입니다.");
                ra.addAttribute("기존 비밀번호와 동일한 비밀번호입니다.");
                return "redirect:/sjhealthy/member/login";
            }
        } catch (Exception e){
            System.out.println("시스템 오류");
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
        return "redirect:/sjhealthy";
    }
}

