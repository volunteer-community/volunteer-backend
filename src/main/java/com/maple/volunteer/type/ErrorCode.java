package com.maple.volunteer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_EXCEPTION("예시 에러 코드 입니다."),
    CATEGORY_NOT_FOUND("해당 ID의 카테고리를 찾을 수 없습니다.");


    private final String description;
}
