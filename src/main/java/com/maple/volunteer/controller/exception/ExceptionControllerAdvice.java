package com.maple.volunteer.controller.exception;

import com.amazonaws.Response;
import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.exception.CommunityUpdateException;
import com.maple.volunteer.exception.ExampleException;
import com.maple.volunteer.exception.NotFoundException;
import com.maple.volunteer.exception.UploadException;
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

    // NotFoundException
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResultDto<Void>> notFoundException(NotFoundException nfe) {
        CommonResponseDto<Object> notFoundException = commonService.errorResponse(nfe.getMessage(), HttpStatus.NOT_FOUND, null);
        ResultDto<Void> result = ResultDto.in(notFoundException.getStatus(), notFoundException.getMessage());

        return ResponseEntity.status(notFoundException.getHttpStatus()).body(result);
    }

    // CommunityUpdateException
    @ExceptionHandler(CommunityUpdateException.class)
    public ResponseEntity<ResultDto<Void>> communityUpdateException(CommunityUpdateException cue) {
        CommonResponseDto<Object> communityUpdateException = commonService.errorResponse(cue.getMessage(), HttpStatus.BAD_REQUEST, null);
        ResultDto<Void> result = ResultDto.in(communityUpdateException.getStatus(), communityUpdateException.getMessage());

        return ResponseEntity.status(communityUpdateException.getHttpStatus()).body(result);
    }

    // UploadException
    @ExceptionHandler(UploadException.class)
    public ResponseEntity<ResultDto<Void>> uploadException(UploadException ule) {
        CommonResponseDto<Object> uploadException = commonService.errorResponse(ule.getMessage(), HttpStatus.BAD_REQUEST, null);
        ResultDto<Void> result = ResultDto.in(uploadException.getStatus(), uploadException.getMessage());

        return ResponseEntity.status(uploadException.getHttpStatus()).body(result);
    }
}
