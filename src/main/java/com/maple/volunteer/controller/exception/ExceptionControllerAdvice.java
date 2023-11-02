package com.maple.volunteer.controller.exception;


import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.common.ResultDto;
import com.maple.volunteer.exception.*;
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

    // NotFoundException
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResultDto<Void>> notFoundException(NotFoundException nfe) {
        CommonResponseDto<Object> notFoundException = commonService.errorResponse(nfe.getMessage(), HttpStatus.NOT_FOUND, null);
        ResultDto<Void> result = ResultDto.in(notFoundException.getStatus(), notFoundException.getMessage());

        return ResponseEntity.status(notFoundException.getHttpStatus()).body(result);
    }

    // BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResultDto<Void>> badRequestException(BadRequestException bre) {
        CommonResponseDto<Object> badRequestException = commonService.errorResponse(bre.getMessage(), HttpStatus.BAD_REQUEST, null);
        ResultDto<Void> result = ResultDto.in(badRequestException.getStatus(), badRequestException.getMessage());

        return ResponseEntity.status(badRequestException.getHttpStatus()).body(result);
    }
}
