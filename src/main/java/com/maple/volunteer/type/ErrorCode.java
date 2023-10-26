package com.maple.volunteer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_EXCEPTION("예시 에러 코드 입니다."),
    CATEGORY_ID_NOT_FOUND("해당 ID의 카테고리를 찾을 수 없습니다."),
    CATEGORY_TYPE_NOT_FOUND("해당 Type의 카테고리를 찾을 수 없습니다."),
    IMAGE_UPLOAD_FAIL("이미지 업로드에 실패 했습니다."),
    FILE_EXTENSION_NOT_FOUND("확장자가 올바르지 않습니다."),
    COMMUNITY_NOT_FOUND("커뮤니티를 찾을 수 없습니다."),
    MAX_PARTICIPANT_LOW_ERROR("참여 인원보다 낮거나 같으면 변경 할 수 없습니다."),
    COMMUNITY_RECRUITMENT_END_ERROR("모집 마감이 된 커뮤니티 입니다.");

    private final String description;
}
