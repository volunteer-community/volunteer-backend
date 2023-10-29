package com.maple.volunteer.service.user;

import com.maple.volunteer.domain.login.Login;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.user.LogoutDto;
import com.maple.volunteer.dto.user.NewTokenDto;
import com.maple.volunteer.dto.user.TokenDto;
import com.maple.volunteer.repository.login.LoginRepository;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.JwtUtil;
import com.maple.volunteer.security.jwt.dto.GeneratedToken;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            User user = userOptional.get();

            Login login = user.getLogin();
            loginRepository.updateRefreshTokenById(login.getId(), token.getRefreshToken());

            TokenDto tokenDto = TokenDto.builder()
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .accessTokenExpireTime(token.getAccessTokenExpireTime())
                    .build();

            return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
        } else {
            return commonService.errorResponse(ErrorCode.USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND, null);
        }
    }

    // 로그아웃
    public CommonResponseDto<Object> logout(String accessToken) {

        // 토큰 통해 email, role get -> 유저 조회
        String email = jwtUtil.getUserEmail(accessToken);
        String role = jwtUtil.getUserRole(accessToken);

        Optional<User> userOptional = userRepository.findByEmail(email);

        // refreshToken -> null 변경
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Login login = user.getLogin();
            loginRepository.updateRefreshTokenById(login.getId(), null);

            // 짧은 accessToken 발급
            String logoutToken = jwtUtil.invalidateToken(email,role);

            LogoutDto logoutDto = LogoutDto.builder()
                    .accessToken(logoutToken)
                    .build();

            return commonService.successResponse(SuccessCode.USER_LOGOUT_SUCCESS.getDescription(), HttpStatus.OK, logoutDto);
        } else {
            return commonService.errorResponse(ErrorCode.USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND, null);
        }
    }

    // accessToken 갱신
    public CommonResponseDto<Object> renewToken(String accessToken) {

        // 토큰 통해 email, role get -> 유저 조회
        String email = jwtUtil.getUserEmail(accessToken);
        String role = jwtUtil.getUserRole(accessToken);

        Optional<User> userOptional = userRepository.findByEmail(email);

        // accessToken 재발급
        if (userOptional.isPresent()) {
            String newToken = jwtUtil.generateNewAccessToken(email, role);
            LocalDateTime accessTokenExpireTime = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);

            NewTokenDto newTokenDto = NewTokenDto.builder()
                    .accessToken(newToken)
                    .accessTokenExpireTime(accessTokenExpireTime)
                    .build();

            return commonService.successResponse(SuccessCode.USER_RENEW_SUCCESS.getDescription(), HttpStatus.OK, newTokenDto);
        } else {
            return commonService.errorResponse(ErrorCode.USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND, null);
        }
    }
}
