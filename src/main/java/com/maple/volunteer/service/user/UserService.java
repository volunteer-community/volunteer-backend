package com.maple.volunteer.service.user;

import com.maple.volunteer.domain.login.Login;
import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.user.SignupDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final CommonService commonService;
    private final JwtUtil jwtUtil;

    public CommonResponseDto<Object> exampleGet() {
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.CREATED, null);
    }

    // 기존회원 로그인
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
                    .build();

            return commonService.successResponse(SuccessCode.USER_LOGIN_SUCCESS.getDescription(), HttpStatus.OK, tokenDto);
        } else {
            return commonService.errorResponse(ErrorCode.USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND, token); //조금 애매
        }
    }

    public CommonResponseDto<Object> signup(SignupDto signupDto) {
        if(findByPhoneNumber(signupDto.getPhoneNumber())){

            return null;
        }else {
            //이미 가입한 핸드폰 번호
            return commonService.successResponse(ErrorCode.EXISTED_PHONE_NUMBER.getDescription(), HttpStatus.BAD_REQUEST, null);
        }
    }

    private boolean findByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user != null;
    }
}
