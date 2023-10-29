package com.maple.volunteer.service.user;

import com.maple.volunteer.domain.login.Login;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.user.TokenDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.login.LoginRepository;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.security.jwt.dto.GeneratedToken;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;

    // 로그인
    public CommonResponseDto<Object> login(String email, String role) {

        // accessToken, refreshToken 발행
        GeneratedToken token = jwtUtil.generateToken(email, role);

        // 기존 refreshToken 변경
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Login login = user.getLogin();
        loginRepository.updateRefreshTokenById(login.getId(), token.getRefreshToken());

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .build();

        return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }

    // 로그아웃
    public CommonResponseDto<Object> logout(String accessToken) {

        // 토큰 통해 email, get -> 유저 조회
        String email = jwtUtil.getUserEmail(accessToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // refreshToken -> null 변경
        Login login = user.getLogin();
        loginRepository.updateRefreshTokenById(login.getId(), null);

        return commonService.successResponse(SuccessCode.USER_LOGOUT_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // accessToken 갱신
    public CommonResponseDto<Object> renewToken(String refreshToken) {

        // 토큰 통해 유저 get
        Login login = loginRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TOKEN_NOT_FOUND));

        User user = login.getUser();
        String email = user.getEmail();
        String role = user.getRole().getKey();

        // accessToken, refreshToken 재발급
        GeneratedToken token = jwtUtil.generateToken(email, role);

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .build();

        return commonService.successResponse(SuccessCode.USER_RENEW_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }
}
