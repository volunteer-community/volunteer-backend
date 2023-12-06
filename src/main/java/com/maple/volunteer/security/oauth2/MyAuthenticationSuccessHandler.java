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

        boolean testEamil = email.equals("1mwdkim1@gmail.com") || email.equals("alsgur990104@gmail.com") || email.equals("koogoori54@gmail.com");

        boolean local = testEamil && provider.equals("google");


        // 회원이 존재하지 않으면
        if (!isExist) {

            String targetUrl = UriComponentsBuilder.fromUriString("https://ecof.site/signup/add")
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

            if (local) {

                TokenDto tokenDto = userService.login(email, provider);

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

            TokenDto tokenDto = userService.login(email, provider);

            String targetUrl = UriComponentsBuilder.fromUriString("https://ecof.site/login/loading")

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
