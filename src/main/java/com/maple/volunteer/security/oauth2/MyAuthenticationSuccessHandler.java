package com.maple.volunteer.security.oauth2;

import com.maple.volunteer.dto.user.TokenDto;
import com.maple.volunteer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

        // 회원이 존재하지 않으면
        if (!isExist) {
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/signup")
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

//        } else {
//            // 이미 로그인 했던 회원
//            TokenDto login = userService.login(email, role, provider, picture);
//            String accessToken = login.getAccessToken();
//            LocalDateTime accessTokenExpiration = login.getAccessTokenExpireTime();
//            String refreshToken = login.getRefreshToken();
//            LocalDateTime refreshTokenExpiration = login.getRefreshTokenExpireTime();
//
//            // 쿠키를 HttpServletResponse에 추가
//            createHttpOnlyCookieWithExpirationDate(response, "accessToken", accessToken, false, false, accessTokenExpiration, "None");
//            createHttpOnlyCookieWithExpirationDate(response, "refreshToken", refreshToken, false, false, refreshTokenExpiration, "None");
//
//            // redirect url
//            response.sendRedirect("http://localhost:3000");
//        }
    }

    // 쿠키 생성 후 response에 추가
    private void createHttpOnlyCookieWithExpirationDate(HttpServletResponse response, String name, String value, boolean secure, boolean httpOnly, LocalDateTime expirationDateTime, String sameSite) {
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime zonedDateTime = expirationDateTime.atZone(seoulZoneId);
        Instant instant = zonedDateTime.toInstant();
        long maxAge = instant.getEpochSecond() - Instant.now().getEpochSecond();


//        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(false);
//        cookie.setDomain("localhost");
//        cookie.setPath("/"); // Set the path according to your requirement
//        cookie.setMaxAge((int) maxAge); // Set the expiration date in seconds from now
//
//        response.addCookie(cookie);

        String cookieValue = String.format("%s=%s; Secure=%b; HttpOnly=%b; Max-Age=%d; Path=/; SameSite=%s", name, value, secure, httpOnly, maxAge, sameSite);
        response.addHeader("Set-Cookie", cookieValue);
    }
}
