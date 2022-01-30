package com.jsh.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jsh.security1.config.auth.PrincipalDetails;
import com.jsh.security1.model.User;
import com.jsh.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //.getAttribute에 이름, 프로필 등 필요한 정보가 들어있다.
        // 이제 이걸 서버내 User 모델로 옮겨야한다.
        // username = google_{sub}
        // password = 상관없음
        // email = {email}
        // role = 'ROLE_USER'

        // 구글 로그인 버튼 -> 구글 로그인 -> Code를 리턴(Oauth2-Client가 받음) -> AccessToken 요청
        // userRequest 정보 -> loadUser함수로 회원 프로필을 받아야함
        System.out.println(super.loadUser(userRequest).getAttributes());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // 회원가입을 강제로 진행해야함
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId; // google_{sub}
        String password = bCryptPasswordEncoder.encode("겟인데어");// 아무거나 상관없음
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
