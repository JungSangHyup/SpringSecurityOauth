package com.jsh.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //.getAttribute에 이름, 프로필 등 필요한 정보가 들어있다.
        // 이제 이걸 서버내 User 모델로 옮겨야한다.
        // username = google_{sub}
        // password = 상관없음
        // email = {email}
        // role = 'ROLE_USER'
        System.out.println(super.loadUser(userRequest).getAttributes());
        // 구글 로그인 버튼 -> 구글 로그인 -> Code를 리턴(Oauth2-Client가 받음) -> AccessToken 요청
        // userRequest 정보 -> 회원 프로필을 받아야함
        return super.loadUser(userRequest);
    }
}
