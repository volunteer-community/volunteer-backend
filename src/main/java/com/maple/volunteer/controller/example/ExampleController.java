package com.maple.volunteer.controller.example;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.dto.example.ExampleDto;
import com.maple.volunteer.service.example.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ExampleController {


    private final ExampleService exampleService;

    // 반환값이 없는 예시 코드 입니다.
    @PostMapping("/example")
    public ResponseEntity<ResultDto<Void>> examplePost() {
        CommonResponseDto<Object> commonResponseDto = exampleService.examplePost();
        ResultDto<Void> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }

    // 반환값이 존재하는 예시 코드 입니다.
    @GetMapping("/example")
    public ResponseEntity<ResultDto<ExampleDto>> exampleGet() {
        CommonResponseDto<Object> commonResponseDto = exampleService.exampleGet();
        ResultDto<ExampleDto> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());
        result.setData((ExampleDto) commonResponseDto.getData());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);

    }
}
