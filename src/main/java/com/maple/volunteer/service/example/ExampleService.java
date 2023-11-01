package com.maple.volunteer.service.example;

import com.maple.volunteer.dto.common.CommonResponseDto;
import com.maple.volunteer.dto.example.ExampleDto;
import com.maple.volunteer.service.common.CommonService;
import com.maple.volunteer.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExampleService {

    private final CommonService commonService;

    // 반환값이 없는 예시 코드 입니다.
    public CommonResponseDto<Object> examplePost() {
        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, null);
    }

    // 반환 값이 존재하는 예시 코드 입니다.
    public CommonResponseDto<Object> exampleGet() {

        ExampleDto exampleDto = new ExampleDto("반환 예시 입니다.");

        return commonService.successResponse(SuccessCode.EXAMPLE_SUCCESS.getDescription(), HttpStatus.OK, exampleDto);
    }
}
