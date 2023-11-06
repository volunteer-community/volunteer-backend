package com.maple.volunteer.security.oauth2;

import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService <OAuth2UserRequest, OAuth2User> {
    // 서버에서 유저 정보 가져오는 역할

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService를 통해 OAuth2User 정보 get
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID, 이름 속성 가져오기
        String registrationProvider = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2User를 통해 OAuth2Attribute 객체 생성
        OAuth2Attribute oAuth2Attribute
                = OAuth2Attribute.of(registrationProvider, userNameAttributeName, oAuth2User.getAttributes());

        // OAuth2Attribute을 Map으로 get
        Map<String, Object> userAttribute = oAuth2Attribute.convertToMap();

        // 사용자 email get 및 회원 여부 판별
        String email = (String) userAttribute.get("email");
        String provider = (String) userAttribute.get("provider");
        Optional<User> findUser = userRepository.findActiveUserByEmailAndProvider(email, provider);

        if(findUser.isEmpty()){
            userAttribute.put("exist", false);

            // 권한 부여 후 userAttribute(email)을 통해 반환
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    userAttribute, "email");
        }

        userAttribute.put("exist", true);

        // 권한과 userAttribute(email)을 통해 DefaultOAuth2User 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttribute, "email");
    }
}
