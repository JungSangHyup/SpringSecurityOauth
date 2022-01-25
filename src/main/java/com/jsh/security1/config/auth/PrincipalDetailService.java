package com.jsh.security1.config.auth;

import com.jsh.security1.model.User;
import com.jsh.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl을 걸어놨기에 /loginProc 요청이 오면 
// UserDetailService 타입으로 Ioc 되어 있는 loadUserByUsername 함수가 실행됨
@Service
public class PrincipalDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session 속에 Authentication(UserDetails))의 정보가 들어간다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
