package com.maple.volunteer.security.oauth2;

import com.maple.volunteer.dto.user.TokenDto;
import com.maple.volunteer.service.user.UserService;
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

    private final UserService userService;
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

        String getScheme = request.getScheme();

        String baseUrl;
        boolean isLocalHost = getScheme.equals("http");

//        if (isLocalHost) {
//            baseUrl = "http://localhost:3000";
//        } else {
//            baseUrl = "https://volunteer-frontend.vercel.app";
//        }

        // 회원이 존재하지 않으면
        if (!isExist) {

            // String targetUrl = UriComponentsBuilder.fromUriString("https://volunteer-frontend.vercel.app/signup/add")
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/signup/add")
                    .queryParam("email", email)
                    .queryParam("provider", provider)
                    .queryParam("role", role)
                    .queryParam("name", name)
                    .queryParam("picture", picture)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {

            TokenDto tokenDto = userService.login(email, provider);

            // String targetUrl = UriComponentsBuilder.fromUriString("https://volunteer-frontend.vercel.app/login/loading")
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/login/loading")

                    .queryParam("trigger", true)
                    .queryParam("accessToken", tokenDto.getAccessToken())
                    .queryParam("accessTokenExpireTime", tokenDto.getAccessTokenExpireTime())
                    .queryParam("refreshToken", tokenDto.getRefreshToken())
                    .queryParam("refreshTokenExpireTime", tokenDto.getRefreshTokenExpireTime())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}
