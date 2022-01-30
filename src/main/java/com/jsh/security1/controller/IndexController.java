package com.jsh.security1.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.jsh.security1.config.auth.PrincipalDetails;
import com.jsh.security1.model.User;
import com.jsh.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    
    
    // 시큐리티 세션내 Authentication 타입은 두 가지가 있다.
    // UserDetails : 일반 로그인으로 할 시
    // OAuth2User : OAuth2 로그인으로 할 시
    // 그러나 이렇게 하는 건 두 가지 방법으로 처리해야되는데 복잡해진다.
    // 따라서 두 가지를 implement한 객체를 따로 생성한다.
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails){ // DI(의존성 주입)
        // 방법1 다운캐스팅 후 적용
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("/test/login => authentication " + principalDetails.getUser());
        
        // 방법2 어노테이션으로 들고오기
        System.out.println("userDetails : " + userDetails.getUsername());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oAuth){ // DI(의존성 주입)
        // 방법1 OAuth는 OAuth2User로 받아야한다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("/test/login => authentication " + oAuth2User.getAttributes());

        // 방법2 어노테이션으로 들고오기
        System.out.println("userDetails : " + oAuth.getAttributes());

        return "세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index(){
        // mustache는 스프링부트 공식 탬플릿 엔진
        // src/main/resources/를 기본으로
        // 뷰리졸버를 /templates 의 mustache로 하자
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails : " + principalDetails);
        return "user";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }
    
    // SecurityConfig 파일을 작동 안함
    @GetMapping("/login")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/join")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/joinProc")
    public String joinProc(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/login";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    //직전에 바로 실행되는 메서드
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }
}
