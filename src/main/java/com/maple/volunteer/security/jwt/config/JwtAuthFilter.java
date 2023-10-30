package com.maple.volunteer.security.jwt.config;

import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.security.jwt.dto.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request Header에서 AccessToken get
        String atc = request.getHeader("Authorization");

        // 토큰 검사 생략
        if (!StringUtils.hasText(atc)) {
            doFilter(request, response, filterChain);
            return;
        }

        // AccessToken을 검증 후 만료시 예외 발생
        if (!jwtUtil.verifyToken(atc)) {
            // AccessToken 내부의 payload에 있는 email로 user 조회. 없으면 예외
            User findUser = userRepository.findByEmail(jwtUtil.getUserId(atc))
                    .orElseThrow(IllegalStateException::new);

            // SecurityContext에 등록할 User 객체 생성
            SecurityUserDto securityUserDto = SecurityUserDto.builder()
                    .id(findUser.getId())
                    .email(findUser.getEmail())
                    .role((findUser.getRole()))
                    .nickname(findUser.getNickname())
                    .build();

            // SecurityContext에 인증 객체 등록
            Authentication authentication = getAuthentication(securityUserDto);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(SecurityUserDto securityUserDto) {
        return new UsernamePasswordAuthenticationToken(securityUserDto, "",
                List.of(new SimpleGrantedAuthority(securityUserDto.getRole().getKey())));
    }
}
