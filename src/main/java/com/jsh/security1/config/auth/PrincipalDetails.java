package com.jsh.security1.config.auth;

// 시큐리티가 /loginProc를 낚아채서 로그인을 진행시킨다.
// 진행이 완료가 되면 시큐리티 session에 넣어야한다. (Security ContextHolder)
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야됨.
// User오브젝트의 타입 => UserDetails 타입 객체
// 시큐리티가 가지는 가지는 Session => Authentication => UserDetails 타입

import com.jsh.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user;
    private Map<String, Object> attributes;
    
    // 일반 로그인
    public PrincipalDetails(User user){
        this.user = user;
    }
    
    // OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User의 권한을 리턴한다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 우리 사이트 1년 접속 안하면 유저 잠그는 옵션 같은것 추가 가능함
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //OAuth2User

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
