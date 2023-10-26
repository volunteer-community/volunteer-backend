package com.maple.volunteer.service.user;

import com.maple.volunteer.domain.user.User;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.user.SignupDto;
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
    private final CommonService commonService;
    private final JwtUtil jwtUtil;

    public CommonResponseDto<Object> exampleGet() {
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.CREATED, null);
    }

    public ResponseEntity<?> login(String email, String role) {

        //1. 먼저 토큰 둘다 발행하기
        GeneratedToken token = jwtUtil.generteToken(email, role);

        //2. 리프레시 토큰 유저pk로 가져오기

        //3-1. 리프레시 토큰 있으면?

        //리프레시토큰 만료되어있는지 확인하기
        //만료 되었으면? 지우고 쌔거 저장하기
        //아니면 말구



        //3-2. 없으면?(없을수가있나?)
        //쌔거 저장하기

        //그다음 리프레시토큰, 엑세스토큰, 이메일?(을 보내줬었던가요?) 을 바디에 담아서 보내주기 (dto하나 만들 것)


        return null;
    }

    public CommonResponseDto<Object> signup(SignupDto signupDto) {
        if(findByPhoneNumber(signupDto.getPhoneNumber())){
            User user = User.builder()
                    .phoneNumber(signupDto.getPhoneNumber())
                    .name(signupDto.getName())
                    .role(signupDto.getRole())
                    .email(signupDto.getEmail())
                    .nickname(signupDto.getNickname())
                    .build();
            userRepository.save(user);


            return commonService.successResponse(SuccessCode.SIGNUP_SUCCESS.getDescription(),HttpStatus.OK,null);
        }else {
            //이미 가입한 핸드폰 번호
            return commonService.errorResponse(ErrorCode.EXISTED_PHONE_NUMBER.getDescription(), HttpStatus.BAD_REQUEST, null);
        }
    }

    private boolean findByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user != null;
    }
}
