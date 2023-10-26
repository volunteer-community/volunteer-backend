package com.maple.volunteer.security.oauth2;

import com.maple.volunteer.security.jwt.JwtUtil;
import com.maple.volunteer.security.jwt.dto.GeneratedToken;
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
    private final JwtUtil jwtUtil;

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

        // 회원이 존재하면
        if (isExist){
            // user 정보를 쿼리스트링에 담는 url 생성
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/maple/user/login")
                    .queryParam("email", email)
                    .queryParam("provider", provider)
                    .queryParam("role", role)
                    .queryParam("name", name)
                    .queryParam("picture", picture)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            // 로그인 확인 페이지로 리다이렉트
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // 회원이 존재하지 않으면
            // provider와 email을 쿼리스트링으로 전달하는 url 생성
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/loginSuccess")
                    .queryParam("email", email)
                    .queryParam("provider", provider)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            // 회원가입 페이지로 리다이렉트
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}
