package com.maple.volunteer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    EXAMPLE_SUCCESS("예시 성공 코드 입니다."),
    CATEGORY_INSERT_SUCCESS("카테고리 등록에 성공 했습니다."),
    CATEGORY_INQUIRY_SUCCESS("카테고리 조회에 성공 했습니다."),
    CATEGORY_UPDATE_SUCCESS("카테고리 수정에 성공 했습니다."),
    CATEGORY_DELETE_SUCCESS("카테고리 삭제에 성공 했습니다."),
    COMMUNITY_CREATE_SUCCESS("커뮤티니 생성에 성공 했습니다."),
    ALL_COMMUNITY_INQUIRY_SUCCESS("모든 커뮤니티 정보 조회에 성공 했습니다."),
    CATEGORY_COMMUNITY_INQUIRY_SUCCESS("카테고리 별 커뮤니티 조회에 성공 했습니다."),
    SEARCH_COMMUNITY_TITLE_INQUIRY_SUCCESS("제목으로 커뮤니티 조회에 성공 했습니다."),
    SEARCH_COMMUNITY_AUTHOR_INQUIRY_SUCCESS("작성자로 커뮤니티 조회에 성공 했습니다."),
    COMMUNITY_DETAIL_INQUIRY_SUCCESS("커뮤니티 상세 정보 조회에 성공 했습니다."),
    COMMUNITY_UPDATE_SUCCESS("커뮤니티 수정에 성공 했습니다."),
    COMMUNITY_DELETE_SUCCESS("커뮤니티 삭제에 성공 했습니다."),
    COMMUNITY_SIGNUP_SUCCESS("커뮤니티 참가에 성공 했습니다."),
    COMMUNITY_WITHDRAW_SUCCESS("커뮤니티 탈퇴에 성공 했습니다.");

    private final String description;
}
