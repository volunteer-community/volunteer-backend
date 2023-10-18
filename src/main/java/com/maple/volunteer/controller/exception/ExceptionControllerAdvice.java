package com.maple.volunteer.controller.exception;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.exception.ExampleException;
import com.maple.volunteer.service.common.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private final CommonService commonService;


    // 오류 반환 예시 코드 입니다.
    @ExceptionHandler(ExampleException.class)
    public ResponseEntity<ResultDto<Void>> exampleException(ExampleException ele) {
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(ele.getMessage(), HttpStatus.BAD_REQUEST, null);
        ResultDto<Void> result = ResultDto.in(commonResponseDto.getStatus(), commonResponseDto.getMessage());

        return ResponseEntity.status(commonResponseDto.getHttpStatus()).body(result);
    }
}
