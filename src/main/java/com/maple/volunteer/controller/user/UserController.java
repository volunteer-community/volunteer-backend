package com.maple.volunteer.controller.user;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.example.ExampleDto;
import com.maple.volunteer.dto.user.SignupDto;
import com.maple.volunteer.security.oauth2.CustomOAuth2UserService;
import com.maple.volunteer.service.user.UserService;
import com.maple.volunteer.type.Role;
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

    //처음 로그인 한 회원 추가정보 받고 회원가입하기
    @PostMapping("/signup")
    public ResponseEntity<ResultDto<SignupDto>> exampleGet(@RequestBody SignupDto signupDto) {
        CommonResponseDto<Object> commonResponseDto = userService.signup(signupDto);
        ResultDto<SignupDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((SignupDto) commonResponseDto.getData());
        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);

    }

    //로그인하기
    @GetMapping("/login")
    public ResponseEntity<?> memberLogin(@RequestParam("email") String email,
                                         @RequestParam("role") String role,
                                         @RequestParam("name") String name,
                                         @RequestParam("picture") String picture) {

        return userService.login(email, role,name,picture);
    }
}
