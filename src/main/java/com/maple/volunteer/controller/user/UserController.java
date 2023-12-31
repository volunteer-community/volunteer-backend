package com.maple.volunteer.controller.user;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.user.CheckDto;
import com.maple.volunteer.dto.user.SignupDto;
import com.maple.volunteer.dto.user.TokenDto;
import com.maple.volunteer.dto.user.ViewUserDto;
import com.maple.volunteer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/maple/user")
public class UserController {

    private final UserService userService;

    // 처음 로그인 한 회원 이메일과 프사 넘겨주기 안쓰겠죠..?
//    @GetMapping("/addInfo")
//    public ResponseEntity<ResultDto<NewUserDto>> addInfo(@RequestParam("email") String email,
//                                                         @RequestParam("picture") String picture,
//                                                         @RequestParam("role") String role,
//                                                         @RequestParam("name")String name,
//                                                         @RequestParam("provider")String provider){
//        CommonResponseDto<Object> commonResponseDto = userService.addinfo(email,picture,role,name,provider);
//        ResultDto<NewUserDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
//        result.setData((NewUserDto) commonResponseDto.getData());
//        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
//    }

//    // 회원가입 시키기기
//    @PostMapping("/signup")
//    public ResponseEntity<ResultDto<TokenDto>> signUp(@RequestBody SignupDto signupDto) {
//        CommonResponseDto<Object> commonResponseDto = userService.signup(signupDto);
//        ResultDto<TokenDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
//        result.setData((TokenDto) commonResponseDto.getData());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.SET_COOKIE, createHttpOnlyCookieWithExpirationDate("accessToken", result.getData().getAccessToken(), true, result.getData().getAccessTokenExpireTime()));
//        headers.add(HttpHeaders.SET_COOKIE, createHttpOnlyCookieWithExpirationDate("refreshToken", result.getData().getRefreshToken(), true, result.getData().getRefreshTokenExpireTime()));
//        return ResponseEntity.status(commonResponseDto.getHttpStatus())
//                .headers(headers)
//                .body(result);
//    }

    // 회원가입 시키기 테스트
    @PostMapping("/signup")
    public ResponseEntity<ResultDto<TokenDto>> signUp(@RequestBody SignupDto signupDto) {
        CommonResponseDto<Object> commonResponseDto = userService.signup(signupDto);
        ResultDto<TokenDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((TokenDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus())
                .body(result);
    }

//    // 로그인 테스트
//    @PostMapping("/login/test")
//    public ResponseEntity<ResultDto<TokenDto>> userLogin(@RequestParam("email") String email,
//                                                         @RequestParam("role") String role,
//                                                         @RequestParam("provider") String provider,
//                                                         @RequestParam("profileImg") String profileImg){ // provider 추가
//
//        CommonResponseDto<Object> login = userService.loginTest(email, role, provider, profileImg);
//        ResultDto<TokenDto> result = ResultDto.in(login.getStatus(), login.getMessage());
//        result.setData((TokenDto) login.getData());
//
//        return ResponseEntity.status(login.getHttpStatus()).body(result);
//    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResultDto<TokenDto>> userLogin(@RequestParam("email") String email,
                                                         @RequestParam("provider") String provider){ // provider 추가

        CommonResponseDto<Object> login = userService.login1(email,provider);
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

//    // 토큰 갱신
//    @PostMapping("/newToken")
//    public ResponseEntity<ResultDto<TokenDto>> renewToken(@RequestHeader("Authorization") String refreshToken){
//
//        CommonResponseDto<Object> renewToken = userService.renewToken(refreshToken);
//        ResultDto<TokenDto> result = ResultDto.in(renewToken.getStatus(), renewToken.getMessage());
//        result.setData((TokenDto) renewToken.getData());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.SET_COOKIE, createHttpOnlyCookieWithExpirationDate("accessToken", result.getData().getAccessToken(), true, result.getData().getAccessTokenExpireTime()));
//        headers.add(HttpHeaders.SET_COOKIE, createHttpOnlyCookieWithExpirationDate("refreshToken", result.getData().getRefreshToken(), true, result.getData().getRefreshTokenExpireTime()));
//
//        return ResponseEntity.status(renewToken.getHttpStatus())
//                .headers(headers)
//                .body(result);
//    }

    // 토큰 갱신
    @PostMapping("/newToken")
    public ResponseEntity<ResultDto<TokenDto>> renewToken(@RequestHeader("Authorization") String refreshToken){

        CommonResponseDto<Object> renewToken = userService.renewToken(refreshToken);
        ResultDto<TokenDto> result = ResultDto.in(renewToken.getStatus(), renewToken.getMessage());
        result.setData((TokenDto) renewToken.getData());

        return ResponseEntity.status(renewToken.getHttpStatus())
                .body(result);
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



    private String createHttpOnlyCookieWithExpirationDate(String name, String value, boolean secure, LocalDateTime expirationDateTime) {
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime zonedDateTime = expirationDateTime.atZone(seoulZoneId);
        Instant instant = zonedDateTime.toInstant();
        long maxAge = instant.getEpochSecond() - Instant.now().getEpochSecond();

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(false)
                .domain("localhost")
                .sameSite("None")
                .path("/") // Set the path according to your requirement
                .maxAge(maxAge) // Set the expiration date in seconds from now
                .build();
        return cookie.toString();
    }
}