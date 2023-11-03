package com.maple.volunteer.controller.user;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.user.*;
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

    // 처음 로그인 한 회원 이메일과 프사 넘겨주기
    @GetMapping("/addInfo")
    public ResponseEntity<ResultDto<NewUserDto>> addInfo(@RequestParam("email") String email,
                                                         @RequestParam("picture") String picture,
                                                         @RequestParam("role") String role,
                                                         @RequestParam("name")String name,
                                                         @RequestParam("provider")String provider){
        CommonResponseDto<Object> commonResponseDto = userService.addinfo(email,picture,role,name,provider);
        ResultDto<NewUserDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((NewUserDto) commonResponseDto.getData());
        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }

    // 회원가입 시키기기
    @PostMapping("/signup")
    public ResponseEntity<ResultDto<TokenDto>> signUp(@RequestBody SignupDto signupDto) {
        CommonResponseDto<Object> commonResponseDto = userService.signup(signupDto);
        ResultDto<TokenDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((TokenDto) commonResponseDto.getData());
        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResultDto<TokenDto>> userLogin(@RequestParam("email") String email,
                                                         @RequestParam("role") String role){

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

    // 닉네임 중복 체크
    @PostMapping("/nicknameCheck")
    public ResponseEntity<ResultDto<CheckDto>> checkNickname(@RequestBody CheckDto checkDto){
        CommonResponseDto<Object> commonResponseDto = userService.nicknameCheck(checkDto);
        ResultDto<CheckDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((CheckDto) commonResponseDto.getData());
        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }

    // 핸드폰 번호 중복 체크
    @PostMapping("/phoneCheck")
    public ResponseEntity<ResultDto<CheckDto>> checkPhone(@RequestBody CheckDto phoneCheckDto){
        CommonResponseDto<Object> commonResponseDto = userService.phoneCheck(phoneCheckDto);
        ResultDto<CheckDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((CheckDto) commonResponseDto.getData());
        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }

    // 유저 정보 수정 페이지에 정보 넘기기
    @PostMapping("/viewUserInfo")
    public ResponseEntity<ResultDto<ViewUserDto>> viewUserInfo(@RequestHeader("Authorization") String accessToken){
        CommonResponseDto<Object> commonResponseDto = userService.viewUserInfo(accessToken);
        ResultDto<ViewUserDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((ViewUserDto) commonResponseDto.getData());
        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }

    // 유저 정보 수정하기
    @GetMapping("/modUserInfo")
    public ResponseEntity<ResultDto<Void>> modUserInfo(@RequestHeader("Authorization") String accessToken,
                                                       @RequestBody ViewUserDto viewUserDto){
        CommonResponseDto<Object> commonResponseDto = userService.modUserInfo(accessToken, viewUserDto);
        ResultDto<Void> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }
}
