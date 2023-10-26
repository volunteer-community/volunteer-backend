package com.maple.volunteer.controller.user;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.example.ExampleDto;
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

    @PostMapping("/signup")
    public ResponseEntity<ResultDto<ExampleDto>> exampleGet() {
        CommonResponseDto<Object> commonResponseDto = userService.exampleGet();
        ResultDto<ExampleDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((ExampleDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);

    }

    @PostMapping("/signin")
    public ResponseEntity<ResultDto<ExampleDto>> exampleGet1() {
        CommonResponseDto<Object> commonResponseDto = userService.exampleGet();
        ResultDto<ExampleDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((ExampleDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);

    }
}
