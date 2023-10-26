package com.maple.volunteer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_EXCEPTION("예시 에러 코드 입니다."),
    CATEGORY_NOT_FOUND("해당 ID의 카테고리를 찾을 수 없습니다."),
    IMAGE_UPLOAD_FAIL("이미지 업로드에 실패 했습니다."),
    FILE_EXTENSION_NOT_FOUND("확장자가 올바르지 않습니다."),
    COMMUNITY_NOT_FOUND("커뮤니티를 찾을 수 없습니다."),
    EXISTED_PHONE_NUMBER("이미 가입된 핸드폰 번호 입니다."),
    EXISTED_NICKNAME("이미 가입된 닉네임입니다.")
    ;

    private final String description;
}
