package com.maple.volunteer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_EXCEPTION("예시 에러 코드 입니다."),

    // 카테고리
    CATEGORY_ID_NOT_FOUND("해당 ID의 카테고리를 찾을 수 없습니다."),
    CATEGORY_TYPE_NOT_FOUND("해당 타입의 카테고리를 찾을 수 없습니다."),

    // 이미지 업로드
    IMAGE_UPLOAD_FAIL("이미지 업로드에 실패 했습니다."),
    FILE_EXTENSION_NOT_FOUND("확장자가 올바르지 않습니다."),


    IMAGE_RESIZING_FAIL("이미지 리사이징에 실패 했습니다."),
    FAIL_CREATE_NEW_FILE("새로운 파일 생성에 실패 했습니다."),
    IMAGE_SIZE_READ_FAIL("이미지 크기를 읽는데 실패 했습니다."),
    FILE_DELETE_FAIL("임시 파일 삭제에 실패 했습니다."),

    // 커뮤니티
    COMMUNITY_NOT_FOUND("커뮤니티를 찾을 수 없습니다."),
    MAX_PARTICIPANT_LOW_ERROR("참여 인원보다 낮으면 변경 할 수 없습니다."),
    COMMUNITY_RECRUITMENT_END_ERROR("모집 마감이 된 커뮤니티 입니다."),
    AUTHOR_NOT_EQUAL("작성자가 일치하지 않습니다."),
    COMMUNITY_USER_NOT_FOUND("커뮤니티 회원 정보를 찾을 수 없습니다."),
    COMMUNITY_USER_DUPLICATE("이미 커뮤니티에 가입된 회원 입니다."),


    // 게시글
    POSTER_NOT_FOUND("게시글을 찾을 수 없습니다."),
    EXISTED_PHONE_NUMBER("이미 가입된 핸드폰 번호 입니다."),
    USER_NOT_FOUND("유저 정보 조회에 실패 했습니다."),
    TOKEN_NOT_VALIDATE("토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND("토큰 조회에 실패 했습니다."),
    EXISTED_NICKNAME("이미 가입된 닉네임입니다."),

    // 댓글
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.");


    private final String description;
}
