package com.jsh.security1.controller;

import com.jsh.security1.model.User;
import com.jsh.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @GetMapping({"", "/"})
    public String index(){
        // mustache는 스프링부트 공식 탬플릿 엔진
        // src/main/resources/를 기본으로
        // 뷰리졸버를 /templates 의 mustache로 하자
        return "index";
    }

    @GetMapping("/user")
    public String user(){
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
