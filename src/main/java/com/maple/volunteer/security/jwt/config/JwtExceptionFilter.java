package com.maple.volunteer.security.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.volunteer.security.jwt.dto.JwtExceptionDto;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.ResponseStatus;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final CommonService commonService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            JwtExceptionDto jwtExceptionDto = JwtExceptionDto.builder()
                    .status(ResponseStatus.FAIL.getDescription())
                    .message(ErrorCode.UNAUTHORIZED_TOKEN.getDescription())
                    .build();
            objectMapper.writeValue(response.getWriter(), jwtExceptionDto);
        }
    }
}
