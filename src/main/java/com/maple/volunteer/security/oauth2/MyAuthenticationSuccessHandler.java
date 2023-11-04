package com.maple.volunteer.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    // 인증 성공 후 처리

    public void onAuthenticationSuccess(HttpServletRequest request
            , HttpServletResponse response, Authentication authentication) throws IOException {

        // OAuth2User로 캐스팅 후 user정보 get
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");
        boolean isExist = oAuth2User.getAttribute("exist");
        String role = oAuth2User.getAuthorities().stream()
                .findFirst() // 권한이 하나씩이므로
                .orElseThrow(IllegalAccessError::new)
                .getAuthority();
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        // 이미 로그인 했던 회원
        if (isExist){
            // user 정보를 쿼리스트링에 담는 url 생성
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/maple/user/login")
                    .queryParam("email", email)
                    .queryParam("role", role)
                    .queryParam("provider", provider)
                    .queryParam("profileImg", picture)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            // 정보 받아서 Controller url 리다이렉트
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // 회원이 존재하지 않으면
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/signup.html")
                    .queryParam("email", email)
                    .queryParam("provider", provider)
                    .queryParam("role", role)
                    .queryParam("name", name)
                    .queryParam("picture", picture)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}
