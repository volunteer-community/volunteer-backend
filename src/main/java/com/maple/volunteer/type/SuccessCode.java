package com.maple.volunteer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
  
    // 카테고리
    CATEGORY_INSERT_SUCCESS("카테고리 등록에 성공 했습니다."),
    CATEGORY_INQUIRY_SUCCESS("카테고리 조회에 성공 했습니다."),
    CATEGORY_UPDATE_SUCCESS("카테고리 수정에 성공 했습니다."),
    CATEGORY_DELETE_SUCCESS("카테고리 삭제에 성공 했습니다."),
  
    // 커뮤니티
    COMMUNITY_CREATE_SUCCESS("커뮤티니 생성에 성공 했습니다."),
    ALL_COMMUNITY_INQUIRY_SUCCESS("모든 커뮤니티 정보 조회에 성공 했습니다."),
    CATEGORY_COMMUNITY_INQUIRY_SUCCESS("카테고리 별 커뮤니티 조회에 성공 했습니다."),
    COMMUNITY_DETAIL_INQUIRY_SUCCESS("커뮤니티 상세 정보 조회에 성공 했습니다."),
    SEARCH_COMMUNITY_TITLE_INQUIRY_SUCCESS("커뮤니티 제목으로 조회에 성공 했습니다."),
    SEARCH_COMMUNITY_AUTHOR_INQUIRY_SUCCESS("커뮤니티 작성자로 조회에 성공 했습니다."),
    COMMUNITY_UPDATE_SUCCESS("커뮤니티 수정에 성공 했습니다."),
    COMMUNITY_DELETE_SUCCESS("커뮤니티 삭제에 성공 했습니다."),
    COMMUNITY_SIGNUP_SUCCESS("커뮤니티 참가에 성공 했습니다."),
    COMMUNITY_RE_SIGNUP_SUCCESS("커뮤니티 재참가에 성공 했습니다."),
    COMMUNITY_WITHDRAW_SUCCESS("커뮤니티 탈퇴에 성공 했습니다."),
    MY_COMMUNITY_CREATED_INQUIRY_SUCCESS("내가 만든 커뮤니티 조회에 성공 했습니다."),
    MY_COMMUNITY_SIGN_INQUIRY_SUCCESS("내가 가입한 커뮤니티 조회에 성공 했습니다."),

    // 게시글
    ALL_POSTER_INQUIRY_SUCCESS("해당 커뮤니티에 작성된 게시글 조회에 성공했습니다"),
    POSTER_DETAIL_INQUIRY_SUCCESS("게시글 상세 정보 조회에 성공했습니다"),
    POSTER_CREATE_SUCCESS("게시글 등록에 성공 했습니다."),
    POSTER_UPDATE_SUCCESS("게시글 수정에 성공 했습니다."),
    POSTER_DELETE_SUCCESS("게시글 삭제에 성공 했습니다."),

    //댓글
    COMMENT_CREATE_SUCCESS("댓글 등록에 성공 했습니다."),
    ALL_COMMENT_INQUIRY_SUCCESS("해당 게시글에 작성된 댓글 조회에 성공했습니다"),
    COMMENT_UPDATE_SUCCESS("댓글 수정에 성공 했습니다."),
    COMMENT_DELETE_SUCCESS("댓글 삭제에 성공 했습니다."),

    // 좋아요
    HEART_TOGGLE_SUCCESS("좋아요 처리에 성공 했습니다."),
    HEART_TOGGLE_CANCEL_SUCCESS("좋아요 취소에 성공 했습니다."),

    // 마이 페이지
    MY_PAGE_INFO_SUCCESS("마이페이지 인포 조회에 성공했습니다."),
    MY_PAGE_USER_WITHDRAWAL_SUCCESS("회원 탈퇴에 성공 했습니다."),
    MY_PAGE_RANK_UP_SUCCESS("등업에 성공 했습니다."),

    // 유저
    USER_LOGIN_SUCCESS("로그인에 성공 했습니다."),
    USER_LOGOUT_SUCCESS("로그아웃에 성공 했습니다."),
    USER_RENEW_SUCCESS("로그인 갱신에 성공 했습니다."),
    SIGNUP_SUCCESS("회원가입이 완료되었습니다."),
    NEW_USER_SUCCESS("처음 로그인하는 회원입니다."),
    ALL_USER_INQUIRY_SUCCESS("전체 회원 조회에 성공 했습니다."),
    NICKNAME_AVAILABLE("사용 가능한 닉네임입니다."),
    NICKNAME_NOT_AVAILABLE("사용 불가능한 닉네임입니다."),
    PHONE_NUMBER_AVAILABLE("사용 가능한 핸드폰번호입니다."),
    PHONE_NUMBER_NOT_AVAILABLE("사용 불가능한 핸드폰번호입니다."),
    VIEW_USERINFO_SUCCESS("사용자 정보 조회에 성공했습니다."),
    MODIFY_USERINFO_SUCCESS("사용자 정보 수정에 성공했습니다.")
    ;
  
    private final String description;
}
