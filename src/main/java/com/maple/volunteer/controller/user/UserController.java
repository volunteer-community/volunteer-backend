package com.maple.volunteer.controller.user;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.user.LogoutDto;
import com.maple.volunteer.dto.user.NewTokenDto;
import com.maple.volunteer.dto.user.SignupDto;
import com.maple.volunteer.dto.user.TokenDto;
import com.maple.volunteer.service.user.UserService;
import com.maple.volunteer.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/maple/user")
public class UserController {

    private final UserService userService;

    //처음 로그인 한 회원 추가정보 받고 회원가입하기
    @PostMapping("/signup")
    public ResponseEntity<ResultDto<SignupDto>> exampleGet(@RequestBody SignupDto signupDto) {
        CommonResponseDto<Object> commonResponseDto = userService.signup(signupDto);
        ResultDto<SignupDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((SignupDto) commonResponseDto.getData());
        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);

    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ResultDto<TokenDto>> userLogin(@RequestParam("email") String email,
                                                           @RequestParam("role") String role) {

        CommonResponseDto<Object> login = userService.login(email, role);
        ResultDto<TokenDto> result = ResultDto.in(login.getStatus(), login.getMessage());
        result.setData((TokenDto) login.getData());

        return ResponseEntity.status(login.getHttpStatus()).body(result);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResultDto<Void>> userLogout(@RequestHeader("Authorization") String accessToken){

        CommonResponseDto<Object> logout = userService.logout(accessToken);
        ResultDto<Void> result = ResultDto.in(logout.getStatus(), logout.getMessage());

        return ResponseEntity.status(logout.getHttpStatus()).body(result);
    }

    // 토큰 갱신
    @PostMapping("/newToken")
    public ResponseEntity<ResultDto<TokenDto>> renewToken(@RequestHeader("Authorization") String refreshToken){

        CommonResponseDto<Object> renewToken = userService.renewToken(refreshToken);
        ResultDto<TokenDto> result = ResultDto.in(renewToken.getStatus(), renewToken.getMessage());
        result.setData((TokenDto) renewToken.getData());

        return ResponseEntity.status(renewToken.getHttpStatus()).body(result);
    }
}
