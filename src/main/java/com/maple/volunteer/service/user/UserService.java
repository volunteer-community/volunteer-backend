package com.maple.volunteer.service.user;

import com.maple.volunteer.domain.login.Login;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.user.*;
import com.maple.volunteer.repository.login.LoginRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;


    // 로그인
    @Transactional
    public CommonResponseDto<Object> login(String email, String provider) {
        // email, provider로 User(false) get
        User user = userRepository.findActiveUserByEmailAndProvider(email, provider)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        String profileImg = user.getProfileImg();
        String role = user.getRole().getKey();

        // profileImg 검사 못함.
//        if (!user.getProfileImg().equals(profileImg)) {
//            user.updateProfileImg(profileImg);
//        }

        Long userId = user.getId();

        // accessToken, refreshToken 발행
        GeneratedToken token = jwtUtil.generateToken(userId, role);

        // 기존 refreshToken 변경
        Login login = user.getLogin();
        loginRepository.updateRefreshTokenById(login.getId(), token.getRefreshToken());

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .refreshTokenExpireTime(token.getRefreshTokenExpireTime())
                .build();

        return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }

    // 로그인 테스트
    @Transactional
    public CommonResponseDto<Object> loginTest(String email, String role, String provider, String profileImg) {
        // email, provider로 User(false) get
        User user = userRepository.findActiveUserByEmailAndProvider(email, provider)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!user.getProfileImg().equals(profileImg)) {
            user.updateProfileImg(profileImg);
        }

        Long userId = user.getId();

        // accessToken, refreshToken 발행
        GeneratedToken token = jwtUtil.generateToken(userId, role);

        // 기존 refreshToken 변경
        Login login = user.getLogin();
        loginRepository.updateRefreshTokenById(login.getId(), token.getRefreshToken());

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .refreshTokenExpireTime(token.getRefreshTokenExpireTime())
                .build();

        return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }

    // 로그아웃
    @Transactional
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
    @Transactional
    public CommonResponseDto<Object> renewToken(String refreshToken) {

        // 토큰 통해 유저 get
        Login login = loginRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TOKEN_NOT_FOUND));

        User user = login.getUser();
        Long userid = user.getId();
        String userRole = user.getRole().getKey();

        // accessToken, refreshToken 재발급
        GeneratedToken token = jwtUtil.generateToken(userid, userRole);

        login.updateRefreshToken(token.getRefreshToken());

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .refreshTokenExpireTime(token.getRefreshTokenExpireTime())
                .build();


        return commonService.successResponse(SuccessCode.USER_RENEW_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }

    // 토큰 갱신 테스트
    @Transactional
    public CommonResponseDto<Object> renewTokenTest(String refreshToken) {

        // 토큰 통해 유저 get
        Login login = loginRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TOKEN_NOT_FOUND));

        User user = login.getUser();
        Long userid = user.getId();
        String userRole = user.getRole().getKey();

        // accessToken, refreshToken 재발급
        GeneratedToken token = jwtUtil.generateToken(userid, userRole);

        login.updateRefreshToken(token.getRefreshToken());

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpireTime(token.getAccessTokenExpireTime())
                .build();


        return commonService.successResponse(SuccessCode.USER_RENEW_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
    }


    // 회원가입
    public CommonResponseDto<Object> signup(SignupDto signupDto) {
        List<User> userList = userRepository.findActiveUserByEmail2(signupDto.getEmail(), signupDto.getProvider());
        if(userList.size()==1)return commonService.errorResponse(ErrorCode.EXIST_USER_EMAIL.getDescription(), HttpStatus.BAD_REQUEST,null);
        else if(userList.size()>1) return commonService.errorResponse(ErrorCode.MULTIPLE_USER_FOUND.getDescription(), HttpStatus.BAD_REQUEST,null);
        //email의 active유저값이 없을때
        if(findByPhoneNumber(signupDto.getPhoneNumber())){
            if(findByNickName(signupDto.getNickname())){
                // email로 User(false) get
                User user = User.builder()
                        .phoneNumber(signupDto.getPhoneNumber())
                        .name(signupDto.getName())
                        .role(signupDto.getRole())
                        .email(signupDto.getEmail())
                        .profileImg(signupDto.getPicture())
                        .nickname(signupDto.getNickname())
                        .provider(signupDto.getProvider())
                        .isDelete(false)
                        .build();
                userRepository.save(user);

                //회원가입한 사람을 로그인시키기
                List<User> loginedUserList = userRepository.findActiveUserByEmail2(signupDto.getEmail(),signupDto.getProvider());
                if(loginedUserList.isEmpty())return commonService.errorResponse(ErrorCode.USER_NOT_FOUND.getDescription(), HttpStatus.BAD_REQUEST,null);
                else if(loginedUserList.size()>1) return commonService.errorResponse(ErrorCode.MULTIPLE_USER_FOUND.getDescription(), HttpStatus.BAD_REQUEST,null);

                User loginUser = loginedUserList.get(0);
                Long userId = loginUser.getId();
                String userRole = loginUser.getRole().getKey();

                GeneratedToken token = jwtUtil.generateToken(userId, userRole);

                Login login = Login.builder()
                        .user(loginUser)
                        .refreshToken(token.getRefreshToken())
                        .build();

                loginRepository.save(login);

                TokenDto tokenDto = TokenDto.builder()
                        .accessToken(token.getAccessToken())
                        .refreshToken(token.getRefreshToken())
                        .accessTokenExpireTime(token.getAccessTokenExpireTime())
                        .refreshTokenExpireTime(token.getRefreshTokenExpireTime())
                        .build();

                return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
            }
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
        Optional<User> userOptional = userRepository.findNickname(nickname);
        return userOptional.isEmpty();
    }

    private boolean findByPhoneNumber(String phoneNumber) {
        Optional<User> userOptional = userRepository.findPhone(phoneNumber);
        return userOptional.isEmpty();
    }

//    public CommonResponseDto<Object> addinfo(String email, String picture, String role, String name, String provider) {
//        NewUserDto newUserDto = new NewUserDto();
//        newUserDto.setEmail(email);
//        newUserDto.setPicture(picture);
//        newUserDto.setRole(role);
//        newUserDto.setName(name);
//        newUserDto.setProvider(provider);
//        return commonService.successResponse(SuccessCode.NEW_USER_SUCCESS.getDescription(), HttpStatus.OK, newUserDto);
//    }

    public CommonResponseDto<Object> nicknameCheck(CheckDto checkDto) {
        Optional<User> userOptional = userRepository.findNickname(checkDto.getCheck());
        if(userOptional.isEmpty()){
            checkDto.setExist(false);
            return commonService.successResponse(SuccessCode.NICKNAME_AVAILABLE.getDescription(), HttpStatus.OK, checkDto);
        }else {
            checkDto.setExist(true);
            return commonService.successResponse(SuccessCode.NICKNAME_NOT_AVAILABLE.getDescription(), HttpStatus.OK, checkDto);
        }
    }

    public CommonResponseDto<Object> phoneCheck(CheckDto phoneCheckDto) {
        Optional<User> userOptional = userRepository.findPhone(phoneCheckDto.getCheck());
        if (userOptional.isEmpty()){
            phoneCheckDto.setExist(false);
            return commonService.successResponse(SuccessCode.PHONE_NUMBER_AVAILABLE.getDescription(), HttpStatus.OK,phoneCheckDto);
        }else {
            phoneCheckDto.setExist(true);
            return commonService.successResponse(SuccessCode.PHONE_NUMBER_NOT_AVAILABLE.getDescription(), HttpStatus.OK,phoneCheckDto);
        }
    }


}