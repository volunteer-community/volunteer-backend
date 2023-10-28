package com.maple.volunteer.controller.user;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.example.ExampleDto;
import com.maple.volunteer.dto.user.SignupDto;
import com.maple.volunteer.dto.user.TokenDto;
import com.maple.volunteer.security.oauth2.CustomOAuth2UserService;
import com.maple.volunteer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/maple/user")
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<ResultDto<Void>> userLogout(@RequestHeader("Email") String email){

        CommonResponseDto<Object> logout = userService.logout(email);
        ResultDto<Void> result = ResultDto.in(logout.getStatus(), logout.getMessage());

        return ResponseEntity.status(logout.getHttpStatus()).body(result);
    }

    // 토큰 갱신
}
