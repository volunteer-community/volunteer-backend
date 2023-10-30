package com.maple.volunteer.service.user;

import com.maple.volunteer.domain.login.Login;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.user.SignupDto;
import com.maple.volunteer.repository.login.LoginRepository;
import com.maple.volunteer.dto.user.TokenDto;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.service.JwtUtil;
import com.maple.volunteer.security.jwt.dto.GeneratedToken;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.ErrorCode;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;


    // 로그인
    public CommonResponseDto<Object> login(String email) {
        // email로 User(false) get
        User user = userRepository.findActiveUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Long userId = user.getId();

        // accessToken, refreshToken 발행
        GeneratedToken token = jwtUtil.generateToken(userId);

        // 기존 refreshToken 변경
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

        // 토큰 통해 userId get -> 유저 조회
        Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // refreshToken -> null 변경
        Login login = user.getLogin();
        loginRepository.updateRefreshTokenById(login.getId(), null);

        return commonService.successResponse(SuccessCode.USER_LOGOUT_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 토큰 갱신
    public CommonResponseDto<Object> renewToken(String refreshToken) {

        // 토큰 통해 유저 get
        Login login = loginRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TOKEN_NOT_FOUND));

        User user = login.getUser();
        Long userid = user.getId();

        // accessToken, refreshToken 재발급
        GeneratedToken token = jwtUtil.generateToken(userid);

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .build();

        return commonService.successResponse(SuccessCode.USER_RENEW_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }

    // 회원가입
    public CommonResponseDto<Object> signup(SignupDto signupDto) {
        if(!findByPhoneNumber(signupDto.getPhoneNumber())){
            if(!findByNickName(signupDto.getName())){
                User user = User.builder()
                        .phoneNumber(signupDto.getPhoneNumber())
                        .name(signupDto.getName())
                        .role(signupDto.getRole())
                        .email(signupDto.getEmail())
                        .nickname(signupDto.getNickname())
                        .build();
                userRepository.save(user);


                return commonService.successResponse(SuccessCode.SIGNUP_SUCCESS.getDescription(),HttpStatus.OK,null);}
            else{
                //이미 가입한 닉네임
                return commonService.errorResponse(ErrorCode.EXISTED_NICKNAME.getDescription(), HttpStatus.BAD_REQUEST, null);
            }
        }else {
            //이미 가입한 핸드폰 번호
            return commonService.errorResponse(ErrorCode.EXISTED_PHONE_NUMBER.getDescription(), HttpStatus.BAD_REQUEST, null);
        }
    }

    private boolean findByNickName(String nickname) {
        User user = userRepository.findByNickname(nickname);
        return user != null;
    }

    private boolean findByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user != null;
    }

    public CommonResponseDto<Object> addinfo(String email, String picture) {
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail(email);
        newUserDto.setPicture(picture);
        return commonService.successResponse(SuccessCode.NEW_USER_SUCCESS.getDescription(), HttpStatus.OK, newUserDto);
    }
}
