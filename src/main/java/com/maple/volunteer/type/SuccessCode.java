package com.maple.volunteer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    EXAMPLE_SUCCESS("예시 성공 코드 입니다."),
  
    // 카테고리
    CATEGORY_INSERT_SUCCESS("카테고리 등록에 성공 했습니다."),
    CATEGORY_INQUIRY_SUCCESS("카테고리 조회에 성공 했습니다."),
    CATEGORY_UPDATE_SUCCESS("카테고리 수정에 성공 했습니다."),
    CATEGORY_DELETE_SUCCESS("카테고리 삭제에 성공 했습니다."),
  
    // 커뮤니티
    COMMUNITY_CREATE_SUCCESS("커뮤티니 생성에 성공 했습니다."),
    ALL_COMMUNITY_INQUIRY_SUCCESS("모든 커뮤니티 정보 조회에 성공 했습니다."),
    CATEGORY_COMMUNITY_INQUIRY_SUCCESS("카테고리 별 커뮤니티 조회에 성공 했습니다."),
    SEARCH_COMMUNITY_INQUIRY_SUCCESS("해당 검색어로 커뮤니티 조회에 성공 했습니다."),
    COMMUNITY_DETAIL_INQUIRY_SUCCESS("커뮤니티 상세 정보 조회에 성공 했습니다."),
    COMMUNITY_UPDATE_SUCCESS("커뮤니티 수정에 성공 했습니다."),
    COMMUNITY_DELETE_SUCCESS("커뮤니티 삭제에 성공 했습니다."),
    COMMUNITY_SIGNUP_SUCCESS("커뮤니티 참가에 성공 했습니다."),
    COMMUNITY_WITHDRAW_SUCCESS("커뮤니티 탈퇴에 성공 했습니다."),

    // 게시글
    ALL_POSTER_INQUIRY_SUCCESS("해당 커뮤니티에 작성된 게시글 조회에 성공했습니다"),
    POSTER_DETAIL_INQUIRY_SUCCESS("게시글 상세 정보 조회에 성공했습니다"),
    POSTER_CREATE_SUCCESS("게시글 등록에 성공 했습니다."),
    POSTER_UPDATE_SUCCESS("게시글 수정에 성공 했습니다."),
    POSTER_DELETE_SUCCESS("게시글 삭제에 성공 했습니다."),

    // 좋아요
    HEART_TOGGLE_SUCCESS("좋아요 처리에 성공 하였습니다"),
    //HEART_CANCLE_SUCCESS("좋아요를 취소하는데 성공 했습니다");

    // 유저
    USER_LOGIN_SUCCESS("로그인에 성공 했습니다."),
    USER_LOGOUT_SUCCESS("로그아웃에 성공 했습니다."),
    USER_RENEW_SUCCESS("로그인 갱신에 성공 했습니다."),
    SIGNUP_SUCCESS("회원가입이 완료되었습니다.");
  
    private final String description;
}
